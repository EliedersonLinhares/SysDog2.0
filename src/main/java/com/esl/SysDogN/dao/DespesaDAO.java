package com.esl.SysDogN.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.esl.SysDogN.model.Despesa;



public class DespesaDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Despesa save(Despesa despesa) {
		return manager.merge(despesa);
	}

	
	public void delete(Despesa despesa) {
	
			despesa = porCodigo(despesa.getCodigo());
			manager.remove(despesa);
			manager.flush();
	}


	public Despesa porCodigo(Long codigo) {
		return manager.find(Despesa.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Despesa> listAll() {
		return manager.createNativeQuery("SELECT * FROM despesa", Despesa.class).getResultList();
}
}

	

