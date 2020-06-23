package com.esl.SysDogN.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortOrder;

import com.esl.SysDogN.dao.AnimalDAO;
import com.esl.SysDogN.dao.ClienteDAO;
import com.esl.SysDogN.model.Animal;
import com.esl.SysDogN.model.Cliente;
import com.esl.SysDogN.util.Transacional;

//Meio do caminho entre dao e controle
public class AnimalService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	@Inject
	private AnimalDAO animalDAO;
	
	
	@Inject
	private ClienteDAO clienteDAO;
	
	
	public List<SelectItem> getListCliente() {

		List<SelectItem> list = new ArrayList<SelectItem>();

		List<Cliente> clientes = clienteDAO.listAll();

		for (Cliente cliente : clientes) {
			list.add(new SelectItem(cliente, cliente.getNome()));
		}
		return list;
	}
	
	
	@Transacional
	public void save(Animal animal) {
		animalDAO.save(animal);
	}
	@Transacional
	public void delete(Animal animal) {
		animalDAO.delete(animal);
	
	}
	
	@SuppressWarnings("unused")
	public Animal porCodigo(Long codigo) {
		return animalDAO.porCodigo(codigo);
	}
	
	@SuppressWarnings("unused")
	public Animal porCodigo2(Long codigo) {
		return animalDAO.porCodigo2(codigo);
	}
	
	
	public List<Animal> listAll() {
		return animalDAO.listAll();
     }
	
	
	public List<Animal> listarLazy(int first, int pageSize,Map<String, Object> filters, String sortField, SortOrder sortOrder){
   
	      CriteriaBuilder cb = manager.getCriteriaBuilder();
	      CriteriaQuery<Animal> criteriaQuery = cb.createQuery(Animal.class);
	      Root<Animal> root = criteriaQuery.from(Animal.class);
	      CriteriaQuery<Animal> select = criteriaQuery.select(root);
	      
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
	     
	      TypedQuery<Animal> query = manager.createQuery(select);
	      query.setFirstResult(first);
	      query.setMaxResults(pageSize);
	      List<Animal> list = query.getResultList();
	      return list;
	      
	      
	     
	}
	
	public Long getTotalRegistros() {
	
		
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Animal.class)));
		 
		return manager.createQuery(criteriaQuery).getSingleResult();
	}
	
	
	public int getColunasFiltradas(Map<String, Object> filters) {
	     
	
	      
		CriteriaBuilder cb = manager.getCriteriaBuilder();
	      CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	      Root<Animal> root = criteriaQuery.from(Animal.class);
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

	 public List<String> descricoesQueContemRaca(String raca) {
			
			TypedQuery<String> query = manager.createQuery(
					"select distinct raca from Animal " + "where upper(raca) like upper(:raca)",
					String.class);
			query.setParameter("raca", "%" + raca + "%");
			return query.getResultList();
		}	
	
	 public Animal buscaporCodigo(Long codigo){
	    	
			EntityManager manager1 = com.esl.SysDogN.util.JpaUtil.getEntityManager();
			
	    	try {
	    		
	    		
	    		CriteriaBuilder criteriaBuilder = manager1.getCriteriaBuilder();
	    		CriteriaQuery<Animal> criteriaQuery = criteriaBuilder.createQuery(Animal.class);
	    		Root<Animal> root = criteriaQuery.from(Animal.class);
	    		criteriaQuery.select(root).where( criteriaBuilder.and( criteriaBuilder.equal(root.get("codigo"), codigo)));
	    		Animal resultado = (Animal) manager1.createQuery(criteriaQuery).getSingleResult();
	    		
	  
	    		return resultado;
	    	}catch(RuntimeException erro) {
	    		throw erro;
	    	}finally {
	    		manager1.close();
	    	}
		}
	 
	 public void merge(Animal animal) {
			AnimalDAO animalDAO1 = new AnimalDAO();
			animalDAO1.merge(animal);
		}
	 
	 
}



