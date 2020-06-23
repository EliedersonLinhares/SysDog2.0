package com.esl.SysDogN.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortOrder;

import com.esl.SysDogN.dao.UsuarioDAO;
import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.util.Transacional;


//Meio do caminho entre dao e controle
public class UsuarioService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	@Inject
	private UsuarioDAO usuarioDAO;
	
	@Transacional
	public void save(Usuario usuario) {
		usuarioDAO.save(usuario);
	}
	
	@Transacional
	public void delete(Usuario usuario) {
		usuarioDAO.delete(usuario);
	}
	
	@SuppressWarnings("unused")
	public Usuario porCodigo(Long codigo) {
		return usuarioDAO.porCodigo(codigo);
	}
	
	public List<Usuario> listAll() {
		return usuarioDAO.listAll();
     }
	
	
	
	public List<Usuario> listarLazy(int first, int pageSize,Map<String, Object> filters, String sortField, SortOrder sortOrder){
   
	      CriteriaBuilder cb = manager.getCriteriaBuilder();
	      CriteriaQuery<Usuario> criteriaQuery = cb.createQuery(Usuario.class);
	      Root<Usuario> root = criteriaQuery.from(Usuario.class);
	      CriteriaQuery<Usuario> select = criteriaQuery.select(root);
	      
	      if (sortField != null) {
	    	  criteriaQuery.orderBy(sortOrder == SortOrder.DESCENDING ?
	                  cb.asc(root.get(sortField)) : cb.desc(root.get(sortField)));
	      }

	      if (filters != null && filters.size() > 0) {
	          List<Predicate> predicates = new ArrayList<>();
	          for (Map.Entry<String, Object> entry : filters.entrySet()) {
	              String field = entry.getKey();
	              Object value = entry.getValue();
	              if (value == null) {
	                  continue;
	              }

	              Expression<String> expr = root.get(field).as(String.class);
	              Predicate p = cb.like(cb.lower(expr),
	                      "%" + value.toString().toLowerCase() + "%");
	              predicates.add(p);
	          }
	          if (predicates.size() > 0) {
	              criteriaQuery.where(cb.and(predicates.toArray
	                      (new Predicate[predicates.size()])));
	          }
	      }
	     
	      TypedQuery<Usuario> query = manager.createQuery(select);
	      query.setFirstResult(first);
	      query.setMaxResults(pageSize);
	      List<Usuario> list = query.getResultList();
	      return list;
	      
	      
	     
	}
	
	public Long getTotalRegistros() {
	
		
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Usuario.class)));
		 
		return manager.createQuery(criteriaQuery).getSingleResult();
	}
	
	
	public int getColunasFiltradas(Map<String, Object> filters) {
	     
	
	      
		CriteriaBuilder cb = manager.getCriteriaBuilder();
	      CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	      Root<Usuario> root = criteriaQuery.from(Usuario.class);
	      CriteriaQuery<Long> select = criteriaQuery.select(cb.count(root));

	      if (filters != null && filters.size() > 0) {
	          List<Predicate> predicates = new ArrayList<>();
	          for (Map.Entry<String, Object> entry : filters.entrySet()) {
	              String field = entry.getKey();
	              Object value = entry.getValue();
	              if (value == null) {
	                  continue;
	              }

	              Expression<String> expr = root.get(field).as(String.class);
	              Predicate p = cb.like(cb.lower(expr),
	                      "%" + value.toString().toLowerCase() + "%");
	              predicates.add(p);
	          }
	          if (predicates.size() > 0) {
	              criteriaQuery.where(cb.and(predicates.toArray
	                      (new Predicate[predicates.size()])));
	          }
	      }
	      Long count = manager.createQuery(select).getSingleResult();
	      return count.intValue();
	  }



	
	public Usuario buscaporEmail(String email){
	    	
		EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		
    	try {
    		
    		
    		CriteriaBuilder criteriaBuilder = manager1.getCriteriaBuilder();
    		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
    		Root<Usuario> root = criteriaQuery.from(Usuario.class);
    		criteriaQuery.select(root).where( criteriaBuilder.and( criteriaBuilder.equal(root.get("email"), email)));
    		Usuario resultado = (Usuario) manager1.createQuery(criteriaQuery).getSingleResult();
    		
  
    		return resultado;
    	}catch(NoResultException nre) {
    	
    		return null;
    	}finally {
    		manager1.close();
    	}
	}
	
	public Usuario buscaporCodigo(Long codigo){
    	
		EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
		
    	try {
    		
    		
    		CriteriaBuilder criteriaBuilder = manager1.getCriteriaBuilder();
    		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
    		Root<Usuario> root = criteriaQuery.from(Usuario.class);
    		criteriaQuery.select(root).where( criteriaBuilder.and( criteriaBuilder.equal(root.get("codigo"), codigo)));
    		Usuario resultado = (Usuario) manager1.createQuery(criteriaQuery).getSingleResult();
    		
  
    		return resultado;
    	}catch(RuntimeException erro) {
    		throw erro;
    	}finally {
    		manager1.close();
    	}
	}
	
	public void merge(Usuario usuario) {
		UsuarioDAO usuarioDAO1 = new UsuarioDAO();
		usuarioDAO1.merge(usuario);
	}
	
	
}



