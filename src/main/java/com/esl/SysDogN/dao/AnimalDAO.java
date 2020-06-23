package com.esl.SysDogN.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.esl.SysDogN.model.Animal;
import com.esl.SysDogN.util.JpaUtil;



public class AnimalDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Animal save(Animal animal) {
		return manager.merge(animal);
	}

	
	public void delete(Animal animal) {
	
			animal = porCodigo(animal.getCodigo());
			manager.remove(animal);
			manager.flush();
	}


	public Animal porCodigo(Long codigo) {
		return manager.find(Animal.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Animal> listAll() {
		return manager.createNativeQuery("SELECT * FROM animal", Animal.class).getResultList();
}
	
	@SuppressWarnings("unchecked")
	public List<Animal> listAll2() {
		EntityManager manager1 = JpaUtil.getEntityManager();
		return manager1.createNativeQuery("SELECT * FROM animal WHERE alocado=false", Animal.class).getResultList();
}
	
	public Animal porCodigo2(Long codigo) {
		EntityManager manager1 = JpaUtil.getEntityManager();
		return manager1.find(Animal.class, codigo);
	}
	
	
	public Animal merge(Animal animal) {
		
		EntityManager manager = JpaUtil.getEntityManager();
		Session sessao = manager.unwrap(Session.class);
		
		Transaction transacao = null;
		
		try {
			transacao = sessao.beginTransaction();
			Animal retorno = (Animal)sessao.merge(animal);
			transacao.commit();
			return retorno;
		}catch(RuntimeException erro){
			if (transacao != null) {
				transacao.rollback();
			}
			throw erro;
		}finally {
			sessao.close();
		

		}
	}
	
}

	

