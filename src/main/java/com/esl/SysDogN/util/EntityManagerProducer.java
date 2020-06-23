package com.esl.SysDogN.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

@ApplicationScoped
public class EntityManagerProducer {
	
	private EntityManagerFactory factory;
	
	
	
	
	public EntityManagerProducer(){
		this.factory = Persistence.createEntityManagerFactory("SysDogN");
	}
	
	/*
	@Produces
	@RequestScoped
	public EntityManager createEntityManager() {
		return this.factory.createEntityManager();
	}
	
	public void closeEntityManager(@Disposes EntityManager manager) {
		manager.close();
	}
	*/
	
	
	//Usando o Session para poder usar o Converter para os combos de chave estrangeiras
	// https://pt.stackoverflow.com/questions/158663/como-buscar-a-session-no-hibernate-5-2-3-final
	@Produces
	@RequestScoped
	public Session createEntityManager() {
	    return (Session) this.factory.createEntityManager();
	}

	public void closeEntityManager(@Disposes Session manager) {
	    manager.close();
	}
	
}


 