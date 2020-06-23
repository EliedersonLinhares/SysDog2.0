package com.esl.SysDogN.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.util.JpaUtil;



public class EstadiaDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Estadia save(Estadia estadia) {
		return manager.merge(estadia);
	}

	
	public void delete(Estadia estadia) {
	
			estadia = porCodigo(estadia.getCodigo());
			manager.remove(estadia);
			manager.flush();
	}


	public Estadia porCodigo(Long codigo) {
		return manager.find(Estadia.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Estadia> listAll() {
		return manager.createNativeQuery("SELECT * FROM estadia", Estadia.class).getResultList();
}
	
	
	public Estadia merge(Estadia estadia) {
		
		EntityManager manager = JpaUtil.getEntityManager();
		Session sessao = manager.unwrap(Session.class);
		
		Transaction transacao = null;
		
		try {
			transacao = sessao.beginTransaction();
			Estadia retorno = (Estadia)sessao.merge(estadia);
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

	

