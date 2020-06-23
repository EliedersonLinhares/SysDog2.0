package com.esl.SysDogN.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortOrder;

import com.esl.SysDogN.dao.AnimalDAO;
import com.esl.SysDogN.dao.EstadiaDAO;
import com.esl.SysDogN.dao.LocalDAO;
import com.esl.SysDogN.model.Animal;
import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.model.Local;
import com.esl.SysDogN.util.JpaUtil;
import com.esl.SysDogN.util.Transacional;


//Meio do caminho entre dao e controle
public class EstadiaService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	

	@Inject
	private EstadiaDAO estadiaDAO;
	
	@Inject
	private LocalDAO localDAO;
	
	
	@Inject
	private AnimalDAO animalDAO;
	
	
	public List<SelectItem> getListLocal() {
		//LocalDAO localDAO = new LocalDAO();
		List<SelectItem> list = new ArrayList<SelectItem>();

		List<Local> locals = localDAO.listAll2();

		for (Local local : locals) {
			list.add(new SelectItem(local, local.getDescricao()));
		}
		return list;
	}
	
	
	public List<SelectItem> getListAnimalDisponivel() {
		//AnimalDAO animalDAO = new AnimalDAO();
		List<SelectItem> list = new ArrayList<SelectItem>();

		List<Animal> animais = animalDAO.listAll2();

		for (Animal animal : animais) {
		
			list.add(new SelectItem(animal, animal.getNome()));
		}
		return list;
	}
	
	public List<SelectItem> getListAnimal() {
		//AnimalDAO animalDAO = new AnimalDAO();
		List<SelectItem> list = new ArrayList<SelectItem>();

		List<Animal> animais = animalDAO.listAll();

		for (Animal animal : animais) {
		
			list.add(new SelectItem(animal, animal.getNome()));
		}
		return list;
	}
	
	
	
	
	@Transacional
	public void save(Estadia estadia) {
		EstadiaDAO estadiaDAO = new EstadiaDAO();
		estadiaDAO.save(estadia);
	
	}
	@Transacional
	public void delete(Estadia estadia) {
		estadiaDAO.delete(estadia);
	}
	
	@SuppressWarnings("unused")
	public Estadia porCodigo(Long codigo) {
	
		return estadiaDAO.porCodigo(codigo);
	}
	
	public List<Estadia> listarLazy(int first, int pageSize,Map<String, Object> filters, String sortField, SortOrder sortOrder){
		
	      CriteriaBuilder cb = manager.getCriteriaBuilder();
	      CriteriaQuery<Estadia> criteriaQuery = cb.createQuery(Estadia.class);
	      Root<Estadia> root = criteriaQuery.from(Estadia.class);
	      CriteriaQuery<Estadia> select = criteriaQuery.select(root);
	      criteriaQuery.orderBy(cb.desc(root.get("dataSaida")));
	      
	      
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
	     
	      TypedQuery<Estadia> query = manager.createQuery(select);
	      query.setFirstResult(first);
	      query.setMaxResults(pageSize);
	      List<Estadia> list = query.getResultList();
	      return list;
	      
	      
	     
	}
	
	public Long getTotalRegistros() {
		
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Estadia.class)));
		 
		return manager.createQuery(criteriaQuery).getSingleResult();
	}
	
	
	public int getColunasFiltradas(Map<String, Object> filters) {
		
	
		CriteriaBuilder cb = manager.getCriteriaBuilder();
	      CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	      Root<Estadia> root = criteriaQuery.from(Estadia.class);
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
	
	
	
	public void merge(Estadia animal) {
		EstadiaDAO animalDAO1 = new EstadiaDAO();
		animalDAO1.merge(animal);
	}
	

/*
 * Modo Lazy para uso no Schedule do primefaces que para funcionar exige uma data de inicio e uma data final para a procura	
 */
	
	
	public List<Estadia> findDates(Date date1, Date date2) {//funcionando teste do Junit
        
		 
		EntityManager manager1 = JpaUtil.getEntityManager();
	       TypedQuery<Estadia> query = manager1.createQuery("select u from Estadia u where u.dataEntrada>= :date1 AND u.dataEntrada<= :date2", 
	               Estadia.class);
	       query.setParameter("date1", date1, TemporalType.TIMESTAMP);
	       query.setParameter("date2", date2, TemporalType.TIMESTAMP);
	       List<Estadia> layoutsSelected = query.getResultList();
	 
	      
	 
	       return layoutsSelected;
	   }
	 
	
	
	
	public static Date getPrimeiraDia(Date inicio){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inicio);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
   
	public static Date getUltimoDia(Date ultima){
        Calendar cal = Calendar.getInstance();
        cal.setTime(ultima);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
	

	public Estadia buscaporCodigo(Long codigo){
    	
		EntityManager manager1 = JpaUtil.getEntityManager();
		
    	try {
    		
    		
    		CriteriaBuilder criteriaBuilder = manager1.getCriteriaBuilder();
    		CriteriaQuery<Estadia> criteriaQuery = criteriaBuilder.createQuery(Estadia.class);
    		Root<Estadia> root = criteriaQuery.from(Estadia.class);
    		criteriaQuery.select(root).where( criteriaBuilder.and( criteriaBuilder.equal(root.get("codigo"), codigo)));
    		Estadia resultado = (Estadia) manager1.createQuery(criteriaQuery).getSingleResult();
    		
  
    		return resultado;
    	}catch(RuntimeException erro) {
    		throw erro;
    	}finally {
    		manager1.close();
    	}
	}
	
	
	
	
}



