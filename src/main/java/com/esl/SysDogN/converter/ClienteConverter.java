package com.esl.SysDogN.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import com.esl.SysDogN.model.Cliente;

@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter, Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String codigo) {
	
		Session session = manager.unwrap(Session.class);
		
		if(codigo != null && !codigo.isEmpty()) {
		return (Cliente)  session.get(Cliente.class, new Long(codigo));
	}
		return codigo;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object objeto) {
		if(objeto != null) {
			Cliente f = (Cliente) objeto;
			return f.getCodigo() != null && f.getCodigo() > 0 ? f.getCodigo().toString() : null;
		}
		return null;
	}

	
	/*Classe para usar no selectonemenu do primefaces, para converter entre a classe e string feito com base 
	 * no video: 
	 * Selectonemenu do Primefaces - Carregando dados do banco
	*
	*  link: https://www.youtube.com/watch?v=PVyC14_GDFA
	*  
	*  Autor: Alex - JDev Treinamento on-line
	*/
}
