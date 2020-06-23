package com.esl.SysDogN.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.esl.SysDogN.model.Local;
import com.esl.SysDogN.util.JpaUtil;



public class LocalDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Local save(Local local) {
		return manager.merge(local);
	}

	
	public void delete(Local local) {
	
			local = porCodigo(local.getCodigo());
			manager.remove(local);
			manager.flush();
	}


	public Local porCodigo(Long codigo) {
		return manager.find(Local.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Local> listAll() {
		return manager.createNativeQuery("SELECT * FROM local", Local.class).getResultList();
}
	
	@SuppressWarnings("unchecked")
	public List<Local> listAll2() {
		EntityManager manager1 = JpaUtil.getEntityManager();
		return manager1.createNativeQuery("SELECT * FROM local", Local.class).getResultList();
}
}

	

