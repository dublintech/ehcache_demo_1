package com.ehcachedemo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

import com.ehcachedemo.pojo.Server;
import com.ehcachedemo.pojo.NonCachedServer;

// some info on hibernate transactions here: https://community.jboss.org/wiki/SessionsAndTransactions
public class HibernateServerDAO {

	
	public HibernateServerDAO() {}
	
	public List<Server> listAllServers() {
		// If you open a session remember to close it.
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println(">> listAllServers(), cacheMode =" + session.getCacheMode());
		Query query = session.createQuery("SELECT S FROM Server S");
		session.beginTransaction();
		List<Server> servers = query.list();
		session.getTransaction().commit();  // automatically closes hibernate session.
		return servers;
	}
	
	public Server getServer(int pk) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Server server = (Server)session.get(Server.class, pk);
		session.getTransaction().commit();
		return server;
	}
	
	public NonCachedServer getNonCachedServer(int pk) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		NonCachedServer server = (NonCachedServer)session.get(NonCachedServer.class, pk);
		session.getTransaction().commit();
		return server;
	}
	

}
