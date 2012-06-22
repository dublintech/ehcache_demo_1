package com.ehcachedemo.dao;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

// uses JMX http://weblogs.java.net/blog/maxpoon/archive/2007/06/extending_the_n_2.html
public class HibernateUtil {
   private static final SessionFactory sessionFactory;
    
  
    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	// Configuration().configure() loads the hibernate hibernate.cfg.xml file
            sessionFactory = new Configuration().configure().buildSessionFactory();
    		Statistics stats = sessionFactory.getStatistics();
    		stats.setStatisticsEnabled(true);
    	    JmxAgent.getDefault(sessionFactory);
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
