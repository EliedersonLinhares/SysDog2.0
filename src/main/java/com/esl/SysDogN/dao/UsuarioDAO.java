package com.esl.SysDogN.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.util.JpaUtil;





public class UsuarioDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Usuario save(Usuario usuario) {
		return manager.merge(usuario);
	}

	
	public void delete(Usuario usuario) {
	
			usuario = porCodigo(usuario.getCodigo());
			manager.remove(usuario);
			manager.flush();
	}


	public Usuario porCodigo(Long codigo) {
		
		
		return manager.find(Usuario.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listAll() {
		return manager.createNativeQuery("SELECT * FROM usuario", Usuario.class).getResultList();
}
	
	
	public Usuario porEmail(String email) {
		EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		return manager1.find(Usuario.class, email);
	}
	
	
public Usuario porCodigoLogado(Long codigo) {
	EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		return manager1.find(Usuario.class, codigo);
	}

public Usuario saveUsuario(Usuario usuario) {
	EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
	return manager1.merge(usuario);
}


public Usuario merge(Usuario usuario) {
	
	EntityManager manager = JpaUtil.getEntityManager();
	Session sessao = manager.unwrap(Session.class);
	
	Transaction transacao = null;
	
	try {
		transacao = sessao.beginTransaction();
		Usuario retorno = (Usuario)sessao.merge(usuario);
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



	

