package com.ehcachedemo.test;

import com.ehcachedemo.dao.*;
import com.ehcachedemo.pojo.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.hibernate.Cache;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;

public class TestHibernateServerDAO {
	
	public void testGetServer() {
		Cache cache = HibernateUtil.getSessionFactory().getCache();
		HibernateServerDAO hsDAO = new HibernateServerDAO();
		// execute something you don't care about just to get rid of first time outlier.
		hsDAO.getServer(187);
		for (int i = 0; i < 50; i++) {
			long beginTime = System.currentTimeMillis();
			Server servers = hsDAO.getServer(186);
			long endTime = System.currentTimeMillis();
			System.out.println("Iteration " + i + ", time = " + (endTime - beginTime));
			// for a laugh check if server with primary key 186 is in cache.
			//System.out.println("in cache=" + 
			//		em.getEntityManagerFactory().getCache().contains(Server.class, 186));
			System.out.println("cache is=" + cache);
			if (i % 5 == 0) {
				cache.evictEntityRegion(Server.class);
			}
		}
	}
	
	public void testGetNonCachedServer() {
		Cache cache = HibernateUtil.getSessionFactory().getCache();
		HibernateServerDAO hsDAO = new HibernateServerDAO();
		// execute something you don't care about just to get rid of first time outlier.
		hsDAO.getNonCachedServer(187);
		for (int i = 0; i < 50; i++) {
			long beginTime = System.currentTimeMillis();
			NonCachedServer server = hsDAO.getNonCachedServer(186);
			long endTime = System.currentTimeMillis();
			System.out.println("Iteration " + i + ", time = " + (endTime - beginTime));
			// for a laugh check if server with primary key 186 is in cache.
			//System.out.println("in cache=" + 
			//		em.getEntityManagerFactory().getCache().contains(Server.class, 186));
			System.out.println("cache is=" + cache);
			if (i % 5 == 0) {
				cache.evictEntityRegion(Server.class);
			}
		}
	}
	
	public void testListAllServers() {
		Cache cache = HibernateUtil.getSessionFactory().getCache();

		// for a laugh check if server with primary key 186 is in cache.
		//System.out.println("in cache=" + 
		//			em.getEntityManagerFactory().getCache().contains(Server.class, 186));
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
