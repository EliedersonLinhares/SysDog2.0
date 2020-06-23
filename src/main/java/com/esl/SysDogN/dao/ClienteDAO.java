package com.esl.SysDogN.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.esl.SysDogN.model.Cliente;



public class ClienteDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Cliente save(Cliente cliente) {
		return manager.merge(cliente);
	}

	
	public void delete(Cliente cliente) {
	
			cliente = porCodigo(cliente.getCodigo());
			manager.remove(cliente);
			manager.flush();
	}


	public Cliente porCodigo(Long codigo) {
		return manager.find(Cliente.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> listAll() {
		return manager.createNativeQuery("SELECT * FROM cliente", Cliente.class).getResultList();
}
}

	

