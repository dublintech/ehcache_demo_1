package com.ehcachedemo.dao;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;
import org.hibernate.SessionFactory;
import org.hibernate.jmx.StatisticsService;


// Taken from http://weblogs.java.net/blog/maxpoon/archive/2007/06/extending_the_n_2.html
public class JmxAgent {
	/**
	   * Register MBeans, enable Hibernate & Ehcache JMX Statistics
	   * @param sf org.hibernate.SessionFactory to be passed in from
	   *           the invoking context (instead of creating it again
	   *           here which is expensive operation)
	   */
	  public void init(SessionFactory sf) throws Exception {
	     
	    // Define ObjectName of the MBean
	    ObjectName on = new ObjectName
	      ("Hibernate:type=statistics,application=HibernateTravelPOJO");

	    // Enable Hibernate JMX Statistics
	    StatisticsService statsMBean = new StatisticsService();
	    statsMBean.setSessionFactory(sf);
	    statsMBean.setStatisticsEnabled(true);
	    mbs.registerMBean(statsMBean, on);
	       
	    /**
	     * Enable Ehcache JMX Statistics
	     * Use CacheManager.getInstance() instead of new CacheManager()
	     * as net.sf.ehcache.hibernate.SingletonEhCacheProvider is used
	     * to ensure reference to the same CacheManager instance as used
	     * by Hibernate
	     */
	    CacheManager cacheMgr = CacheManager.getInstance();
	    ManagementService.registerMBeans
	      (cacheMgr, mbs, true, true, true, true);
	  }
	   
	  /**
	   * Returns an agent singleton.
	   */
	  public synchronized static JmxAgent getDefault(SessionFactory sf)
	      throws Exception {
	    if(singleton == null) {
	      singleton = new JmxAgent();
	      singleton.init(sf);
	    }
	    return singleton;
	  }
	   
	  public MBeanServer getMBeanServer() {
	    return mbs;
	  }
	   
	  // Platform MBeanServer used to register your MBeans
	  private final MBeanServer mbs =
	      ManagementFactory.getPlatformMBeanServer();
	   
	  // Singleton instance
	  private static JmxAgent singleton;
}
