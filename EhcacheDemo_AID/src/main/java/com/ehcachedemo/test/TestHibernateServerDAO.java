package com.ehcachedemo.test;

import com.ehcachedemo.dao.*;
import com.ehcachedemo.pojo.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.hibernate.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.Test;

import org.junit.Test;


/**
 * Tests Hibernate Second level Caching.
 * <P>
 * Hibernate maintains two levels of cache: first-level and second-level cache. 
 * The first-level cache is responsible for storing the results within a particular session instance, while the second-level 
 * cache is associated with the SessionFactory instance.
 * <P>
 * The second-level cache can be enabled or disabled by setting the property hibernate.cache.use_second_level_cache to
 * true (the default for classes that specify <cache> mapping) or false, respectively.
 * <P>
 * You can choose which implementation to use for an application by setting the hibernate.cache.provider_class property 
 * in the hibernate.cfg.xml file as follows.
 * <Pre>
 * <property name="hibernate.cache.provider_class">org.hibernate.cache.EhCacheProvider</property>
 * </pre>
 * <P>
 * Caching can also be configured for each entity by annotating the annotation as:
 * <pre>
 * @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
 * </pre>
 * 
 * <P>
 * References:
 * <UL>
 * <LI>http://www.developer.com/java/ent/article.php/10933_3892261_2/JPA-20-Cache-Vs-Hibernate-Cache-Differences-in-Approach.htm
 * <LI>http://docs.jboss.org/hibernate/orm/4.0/devguide/en-US/html/ch06.html
 * </UL>
 * 
 * Next up:
 * 1. Write multi-threaded read
 * 2. Write tests
 * 3. See if there is any way to make database slower.
 * 4. Go thru various cache config options.
 * 
 * @author Alex
 *
 */
public class TestHibernateServerDAO {
	
	@Test
	public void testGetServer() {
		Cache cache = HibernateUtil.getSessionFactory().getCache();
		HibernateServerDAO hsDAO = new HibernateServerDAO();
		for (int i = 0; i < 50; i++) {
			long beginTime = System.currentTimeMillis();
			System.out.println("Cache contains server=" + cache.containsEntity(Server.class, 186));
			Server server = hsDAO.getServer(186);
			long endTime = System.currentTimeMillis();
			System.out.println("Iteration " + i + ", time = " + (endTime - beginTime));
			if (i % 5 == 0) {
				System.out.println("Eviction happeing");
				cache.evictEntityRegion(Server.class);
			}
		}
	}
	
	@Test
	public void testGetNonCachedServer() {
		Cache cache = HibernateUtil.getSessionFactory().getCache();
		HibernateServerDAO hsDAO = new HibernateServerDAO();
		for (int i = 0; i < 50; i++) {
			System.out.println("Cache contains server=" + cache.containsEntity(NonCachedServer.class, 186));
			long beginTime = System.currentTimeMillis();
			NonCachedServer server = hsDAO.getNonCachedServer(186);
			long endTime = System.currentTimeMillis();
			System.out.println("Iteration " + i + ", time = " + (endTime - beginTime));
			if (i % 5 == 0) {
				cache.evictEntityRegion(Server.class);
			}
		}
	}
	
	@Test
	public void testListAllServers() {
		Cache cache = HibernateUtil.getSessionFactory().getCache();
		HibernateServerDAO hsDAO = new HibernateServerDAO();
		for (int i = 0; i < 20; i++) {
			long beginTime = System.currentTimeMillis();
			List servers = hsDAO.listAllServers();
			long endTime = System.currentTimeMillis();
			System.out.println("Iteration " + i + ", size of servers is=" + servers.size() + ", time = " + (endTime - beginTime));
			// for a laugh check if server with primary key 186 is in cache.
			//System.out.println("in cache=" + 
			//		em.getEntityManagerFactory().getCache().contains(Server.class, 186));
			System.out.println("cache is=" + cache);
			if (i % 5 == 0) {
				cache.evictEntityRegion(Server.class);
			}
		}
	}
	
	
	public void printHibernateStats() {
		Statistics stats = HibernateUtil.getSessionFactory().getStatistics();

		System.out.println("Main stats=" + stats);
		double queryCacheHitCount  = stats.getQueryCacheHitCount();
		double queryCacheMissCount = stats.getQueryCacheMissCount();
		double queryCacheHitRatio =
		  queryCacheHitCount / (queryCacheHitCount + queryCacheMissCount);

		System.out.println("Query Hit ratio:" + queryCacheHitRatio);

		EntityStatistics entityStats =
		  stats.getEntityStatistics(Server.class.getName() );
		long changes =
		        entityStats.getInsertCount()
		        + entityStats.getUpdateCount()
		        + entityStats.getDeleteCount();
		System.out.println(Server.class.getName() + " changed " + changes + "times"  );
		
		System.out.println("Second Level..." + stats.getSecondLevelCacheHitCount());
		System.out.println("Second Level..." + stats.getSecondLevelCacheMissCount());
	}

	

}
