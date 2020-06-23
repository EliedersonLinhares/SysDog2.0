package com.esl.SysDogN.service;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.esl.SysDogN.dao.ResetarSenhaDAO;
import com.esl.SysDogN.model.ResetarSenha;
import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.util.Transacional;

public class ResetarSenhaService implements Serializable{
	private static final long serialVersionUID = 1L;
	


	
	//@Inject
	//private ResetarSenhaDAO resetarSenhaDAO;
	
	ResetarSenhaDAO resetarSenhaDAO = new ResetarSenhaDAO();
	
	public void save(ResetarSenha resetarSenha) {
		resetarSenhaDAO.merge(resetarSenha);
	}
	
	public ResetarSenha buscaPorUsuario(Usuario usuario) {
		return resetarSenhaDAO.buscaPorUsuario(usuario);
	}
	
	 public void excluirPorCodigo(Long codigo) {
		 resetarSenhaDAO.excluirPoCodigo(codigo);
	 }

	 public ResetarSenha buscaPorhash(String hash) {
			return resetarSenhaDAO.buscaporHash(hash);
		}
	
}
