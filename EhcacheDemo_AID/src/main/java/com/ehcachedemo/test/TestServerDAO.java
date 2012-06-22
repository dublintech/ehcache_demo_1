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

public class TestServerDAO {
	private EntityManagerFactory entityManagerFactory;
	private ServerDAO serverDAO;
	
	public void testListAllServers() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		
		// for a laugh check if server with primary key 186 is in cache.
		System.out.println("in cache=" + 
					em.getEntityManagerFactory().getCache().contains(Server.class, 186));
		for (int i = 0; i < 20; i++) {
	
			em.getTransaction().begin();
			long beginTime = System.currentTimeMillis();
			List<Server> servers = serverDAO.listAllServersNoTx();
			long endTime = System.currentTimeMillis();
			em.getTransaction().commit();
			System.out.println("time taken=" + (endTime - beginTime));
			System.out.println("servers"  + servers);
			if (i % 5 == 0) {
				em.getEntityManagerFactory().getCache().evictAll();
			}
			// for a laugh check if server with primary key 186 is in cache.
			System.out.println("in cache=" + 
					em.getEntityManagerFactory().getCache().contains(Server.class, 186));
		}
	}
	
	// only called for set up.
	public void setUpServers() {
		ServerDAO serverDAO = new ServerDAO();
		for (int i = 0; i < 500; i++) {
			Server server = new Server();
			server.setId("Server" + i);
			server.setName("Server" + i);
			serverDAO.persistServer(server);
		}
	}

	public void setUpNonCachedServers() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		for (int i = 0; i < 500; i++) {
			NonCachedServer server = new NonCachedServer();
			server.setId("Server" + i);
			server.setName("Server" + i);
			em.persist(server);
		}
		em.getTransaction().commit();
	}
	
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
	
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}
	
	public void setServerDAO(ServerDAO serverDAO) {
		this.serverDAO = serverDAO;
	}
	
	public ServerDAO getServerDAO() {
		return this.serverDAO;
	}
}
