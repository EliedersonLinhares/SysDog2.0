package com.esl.SysDogN.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortOrder;

import com.esl.SysDogN.dao.ClienteDAO;
import com.esl.SysDogN.model.Cliente;
import com.esl.SysDogN.util.Transacional;

//Meio do caminho entre dao e controle
public class ClienteService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	@Inject
	private ClienteDAO clienteDAO;
	
	@Transacional
	public void save(Cliente cliente) {
		clienteDAO.save(cliente);
	}
	
	@Transacional
	public void delete(Cliente cliente) {
		clienteDAO.delete(cliente);
	}
	
	@SuppressWarnings("unused")
	public Cliente porCodigo(Long codigo) {
		return clienteDAO.porCodigo(codigo);
	}
	
	public List<Cliente> listAll() {
		return clienteDAO.listAll();
     }
	
	
	public List<Cliente> listarLazy(int first, int pageSize,Map<String, Object> filters, String sortField, SortOrder sortOrder){
   
	      CriteriaBuilder cb = manager.getCriteriaBuilder();
	      CriteriaQuery<Cliente> criteriaQuery = cb.createQuery(Cliente.class);
	      Root<Cliente> root = criteriaQuery.from(Cliente.class);
	      CriteriaQuery<Cliente> select = criteriaQuery.select(root);
	      criteriaQuery.orderBy(cb.desc(root.get("dataCadastro")));
	      
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
	     
	      TypedQuery<Cliente> query = manager.createQuery(select);
	      query.setFirstResult(first);
	      query.setMaxResults(pageSize);
	      List<Cliente> list = query.getResultList();
	      return list;
	      
	      
	     
	}
	
	public Long getTotalRegistros() {
	
		
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Cliente.class)));
		 
		return manager.createQuery(criteriaQuery).getSingleResult();
	}
	
	
	public int getColunasFiltradas(Map<String, Object> filters) {
	     
	
	      
		CriteriaBuilder cb = manager.getCriteriaBuilder();
	      CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	      Root<Cliente> root = criteriaQuery.from(Cliente.class);
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

	 public List<String> descricoesQueContemBairro(String bairro) {
			
			TypedQuery<String> query = manager.createQuery(
					"select distinct bairro from Cliente " + "where upper(bairro) like upper(:bairro)",
					String.class);
			query.setParameter("bairro", "%" + bairro + "%");
			return query.getResultList();
		}	

	 public List<String> descricoesQueContemCidade(String cidade) {
			
			TypedQuery<String> query = manager.createQuery(
					"select distinct cidade from Cliente " + "where upper(cidade) like upper(:cidade)",
					String.class);
			query.setParameter("cidade", "%" + cidade + "%");
			return query.getResultList();
		}	
	
}



