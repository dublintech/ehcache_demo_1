package com.ehcachedemo.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ehcachedemo.dao.HibernateUtil;

import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.junit.Test;

/**
 * <B>EhCache Threads</B>
 * In this example, EhCache spawns two threads:
 * <OL>
 * <LI>Daemon Thread: net.sf.ehcache.CacheManager
 * <LI>Non Daemon Thread: com.ehcachedemo.pojo.Server.data
 * </OL>
 * when it is configured as: 
 * <pre>
 * overflowToDisk="true"
 * </pre>
 * When, 
 * <pre>
 * overflowToDisk="true"
 * </pre>
 * , EhCache spawns one thread w
 * 
 * 
 *  
 * <B>EhCache Shutdown</B>
 * http://ehcache.org/documentation/operations/shutdown
 * 
 * <B>good links</B>
 * Info on EhCache - aggregate http://twasink.net/2005/10/20/ehcache-dissected/
 * 
 * @author Alex
 * 
 *
 */
public class TestRunner {

	@Test
	public void testHibernate_EH_Cache() {
		// output cache name
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		String cacheIntegrationName = ((SessionFactoryImplementor)sessionFactory).getSettings().getRegionFactory()
			    .getClass()
			    .getName();
		
		System.out.println(">> cacheIntegrationName=" + cacheIntegrationName);
		
		TestHibernateServerDAO testHibernateServerDAO = 
				new TestHibernateServerDAO();
		testHibernateServerDAO.testGetServer();
		testHibernateServerDAO.testGetNonCachedServer();
		testHibernateServerDAO.testListAllServers();
		testHibernateServerDAO.printHibernateStats();
		HibernateUtil.getSessionFactory().close();
	}
	
}
