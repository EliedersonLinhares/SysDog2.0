package com.esl.SysDogN.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.esl.SysDogN.model.ResetarSenha;
import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.util.JpaUtil;
import com.esl.SysDogN.util.Transacional;

public class ResetarSenhaDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public ResetarSenha ProcuraResetporCodigo(Long codigo) {
		EntityManager manager = com.esl.SysDogN.util.JpaUtil.getEntityManager();

		return manager.find(ResetarSenha.class, codigo);
	}
	
	
	public ResetarSenha save(ResetarSenha resetarSenha) {
		return manager.merge(resetarSenha);	
	}
	

	public ResetarSenha merge(ResetarSenha resetarSenha) {
		
		EntityManager manager = JpaUtil.getEntityManager();
		Session sessao = manager.unwrap(Session.class);
		
		Transaction transacao = null;
		
		try {
			transacao = sessao.beginTransaction();
			ResetarSenha retorno = (ResetarSenha)sessao.merge(resetarSenha);
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
	

	

    public void excluirPoCodigo(Long codigo) {//Funcionando
    	EntityManager manager = JpaUtil.getEntityManager();
		Session sessao = manager.unwrap(Session.class);
    	Transaction transacao = null;//inicia ela como nula
    	
    	try {
    		
    		
    		transacao = sessao.getTransaction();
    		transacao.begin();
    		//Delete a persistent object
    		ResetarSenha resetarSenha1= sessao.get(ResetarSenha.class, codigo);   
    		sessao.delete(resetarSenha1);
             
    		
    		transacao.commit();
    	}catch(RuntimeException erro){//tratamento de erro
    		if (transacao != null) {
    			transacao.rollback();
             }
             erro.printStackTrace();
    	}finally {
    		sessao.close();//fecha a sessão em qualquer caso
    	}
    }
	
	
	public ResetarSenha buscaporHash(String hash){
		
		EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		
		try {
			
			
			CriteriaBuilder criteriaBuilder = manager1.getCriteriaBuilder();
			CriteriaQuery<ResetarSenha> criteriaQuery = criteriaBuilder.createQuery(ResetarSenha.class);
			Root<ResetarSenha> root = criteriaQuery.from(ResetarSenha.class);
			criteriaQuery.select(root).where( criteriaBuilder.and( criteriaBuilder.equal(root.get("hash"), hash)));
			ResetarSenha resultado = (ResetarSenha) manager1.createQuery(criteriaQuery).getSingleResult();
			

			return resultado;
		}catch(RuntimeException erro) {
			return null;
		}finally {
			manager1.close();
		}
	}
	
public ResetarSenha buscaPorUsuario(Usuario usuario){
		
		EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		
		try {
			
			
			CriteriaBuilder criteriaBuilder = manager1.getCriteriaBuilder();
			CriteriaQuery<ResetarSenha> criteriaQuery = criteriaBuilder.createQuery(ResetarSenha.class);
			Root<ResetarSenha> root = criteriaQuery.from(ResetarSenha.class);
			criteriaQuery.select(root).where( criteriaBuilder.and( criteriaBuilder.equal(root.get("usuario"), usuario)));
			ResetarSenha resultado = (ResetarSenha) manager1.createQuery(criteriaQuery).getSingleResult();
		

			return resultado;
			
			
		}catch(RuntimeException erro) {
			
			throw erro;
		}finally {
			manager1.close();
		}
	}
public Boolean buscaPorUsuario2(Usuario usuario){
	
	EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
	
	try {
		
		
		CriteriaBuilder criteriaBuilder = manager1.getCriteriaBuilder();
		CriteriaQuery<ResetarSenha> criteriaQuery = criteriaBuilder.createQuery(ResetarSenha.class);
		Root<ResetarSenha> root = criteriaQuery.from(ResetarSenha.class);
		criteriaQuery.select(root).where( criteriaBuilder.and( criteriaBuilder.equal(root.get("usuario"), usuario)));
		ResetarSenha resultado = (ResetarSenha) manager1.createQuery(criteriaQuery).getSingleResult();
		
		if(resultado == null) {
			return false;
		}

		return true;
		
		
	}catch(RuntimeException erro) {
		
		throw erro;
	}finally {
		manager1.close();
	}
}
	
	
}
