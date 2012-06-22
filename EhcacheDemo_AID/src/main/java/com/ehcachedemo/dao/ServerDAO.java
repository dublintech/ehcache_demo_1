package com.ehcachedemo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import com.ehcachedemo.pojo.Server;

public class ServerDAO {

	EntityManagerFactory entityManagerFactory = null;
	
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory){
		this.entityManagerFactory = entityManagerFactory;
	}
	
	private EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void persistServer(Server server) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist(server);
		em.getTransaction().commit();
		em.close();
	}
	
	public List<Server> listAllServers() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		TypedQuery<Server> query = em.createQuery("SELECT S FROM Server S", Server.class);
		em.getTransaction().begin();
		List<Server> results = query.getResultList();
		em.getTransaction().commit();
		return results;
	}
	
	public List<Server> listAllServersNoTx() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		TypedQuery<Server> query = em.createQuery("SELECT S FROM Server S", Server.class);
		List<Server> results = query.getResultList();
		return results;
	}
	
	public void deleteServer(Server server) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.remove(server);
		em.getTransaction().commit();
		em.close();
	}
	
	public int deleteServer(String id){
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query query = em.createNamedQuery("deleteServerByName");
		query.setParameter("id", id);
		em.getTransaction().begin();
		int result = query.executeUpdate();
		em.getTransaction().commit();
		return result;
	}
	
	public int getNumberOfServers() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Query query = em.createNamedQuery("countServes");
		em.getTransaction().begin();
		Number result = (Number)query.getSingleResult();
		em.getTransaction().commit();
		return result.intValue();
	}
	

	public void updateServerName(String id, String newName) {
		// Step 1 get that server.
		EntityManager em = getEntityManagerFactory().createEntityManager();
		TypedQuery<Server> tquery = em.createNamedQuery("findAllServersById", Server.class);
		tquery.setParameter("id", id);
		Server server = tquery.getSingleResult();
		// Step 2 change name and persist.
		server.setName(newName);
		em.getTransaction().begin();
		em.persist(server);
		em.getTransaction().commit();
	}
	
	
	public Server readServer(String id) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		TypedQuery<Server> tquery = em.createNamedQuery("findAllServersById", Server.class);
		tquery.setParameter("id", id);
		Server server = tquery.getSingleResult();
		return server;
	}
	
	
	/**
	 * Useful for debuggging problems.
	 */
	private void tryJDBCConnect() {
		try {
			Class.forName("com.mysql.jdbc.Drive");
			String url = "jdbc:mysql://localhost:3306/opsource"; 
			Connection conn = DriverManager.getConnection(url, "root", "Euan");
			System.out.println("conn is..." + conn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
