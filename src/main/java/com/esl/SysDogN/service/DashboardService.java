package com.esl.SysDogN.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.esl.SysDogN.model.Despesa;
import com.esl.SysDogN.model.Estadia;

public class DashboardService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private  EntityManager manager;
	
public BigDecimal TotalEstadiaMensal(Date date1 , Date date2) {
		
		
		
		TypedQuery<BigDecimal> query = manager.createQuery(
				"select SUM(u.total) from Estadia u where u.dataEntrada>= :date1 AND u.dataEntrada<= :date2",
				BigDecimal.class);
		 query.setParameter("date1", date1, TemporalType.TIMESTAMP);
	       query.setParameter("date2", date2, TemporalType.TIMESTAMP);
		return query.getSingleResult();
		
}
	
	
public  BigDecimal TotalDespesaMensal(Date date1 , Date date2) {
	

	
	TypedQuery<BigDecimal> query = manager.createQuery(
			"select SUM(u.valor) from Despesa u where u.dataPagamento>= :date1 AND u.dataPagamento<= :date2",
			BigDecimal.class);
	 query.setParameter("date1", date1, TemporalType.DATE);
       query.setParameter("date2", date2, TemporalType.DATE);
	return query.getSingleResult();
	
}



public  int EstadiasAbertas(Date date1 , Date date2) {
	
 
	Boolean status = new Boolean(true);
	
	TypedQuery<Long> query = manager.createQuery(
			"select COUNT(*) from Estadia u where u.status=:status1 and u.dataEntrada>= :date1 AND u.dataEntrada<= :date2",
			Long.class);
	 query.setParameter("status1", status);
	 query.setParameter("date1", date1, TemporalType.DATE);
     query.setParameter("date2", date2, TemporalType.DATE);
     
    return query.getSingleResult().intValue();
}
	

public BigDecimal TotalDespesaMensalTipo(Date date1 , Date date2, String tipo) {
	
	
	
	TypedQuery<BigDecimal> query = manager.createQuery(
			"select SUM(u.valor) from Despesa u where u.tipo=:tipo1 and u.dataPagamento>= :date1 AND u.dataPagamento<= :date2",
			BigDecimal.class);
	 query.setParameter("tipo1", tipo);
	 query.setParameter("date1", date1, TemporalType.DATE);
     query.setParameter("date2", date2, TemporalType.DATE);
	return query.getSingleResult();
	
}


public List<Despesa> listarLazyDespesaD(int first, int pageSize,Map<String, Object> filters, String sortField, SortOrder sortOrder){

	 Calendar cal1 = Calendar.getInstance();
      cal1.add(Calendar.DAY_OF_MONTH,0);
	 
      Date date1 =  cal1.getTime();
	
      Calendar cal2 = Calendar.getInstance();
      cal2.add(Calendar.DAY_OF_MONTH,5);
	 
      Date date2 =  cal2.getTime();
	
    CriteriaBuilder cb = manager.getCriteriaBuilder();
    CriteriaQuery<Despesa> criteriaQuery = cb.createQuery(Despesa.class);
    Root<Despesa> root = criteriaQuery.from(Despesa.class);
    CriteriaQuery<Despesa> select = criteriaQuery.select(root);
   
   // criteriaQuery.where(cb.between(root.get("dataVencimento"), date1, date2),cb.equal(root.get("status"), "EM ABERTO"));
    
   Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("dataVencimento"), date1);
   Predicate endDatePredicate = cb.lessThanOrEqualTo(root.get("dataVencimento"), date2);
   Predicate status = cb.equal(root.get("status"), "EM ABERTO") ;
    
    
    Predicate and = cb.and(startDatePredicate,endDatePredicate,status);
    criteriaQuery.where(and);
    criteriaQuery.orderBy(cb.asc(root.get("dataVencimento")));
    
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
   
    TypedQuery<Despesa> query = manager.createQuery(select);
    query.setFirstResult(first);
    query.setMaxResults(pageSize);
    List<Despesa> list = query.getResultList();
    return list;
    
    
   
}

public Long getTotalRegistrosDespesaD() {
	
	Calendar cal1 = Calendar.getInstance();
    cal1.add(Calendar.DAY_OF_MONTH,0);
	 
    Date date1 =  cal1.getTime();
	
    Calendar cal2 = Calendar.getInstance();
    cal2.add(Calendar.DAY_OF_MONTH,5);
	 
    Date date2 =  cal2.getTime();
	
	
	
	CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
	CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
	Root<Despesa> root = criteriaQuery.from(Despesa.class);
	criteriaQuery.select(criteriaBuilder.count(root));
	
	 Predicate startDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), date1);
	   Predicate endDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), date2);
	   Predicate status = criteriaBuilder.equal(root.get("status"), "EM ABERTO") ;
	    
	    
	    Predicate and = criteriaBuilder.and(startDatePredicate,endDatePredicate,status);
	    criteriaQuery.where(and);
	 
	return manager.createQuery(criteriaQuery).getSingleResult();
}


public int getColunasFiltradasDespesaD(Map<String, Object> filters) {
    
	CriteriaBuilder cb = manager.getCriteriaBuilder();
    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
    Root<Despesa> root = criteriaQuery.from(Despesa.class);
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

public List<Estadia> listarEstadiaD(){
	
	EntityManager manager = com.esl.SysDogN.util.JpaUtil.getEntityManager();

	 Calendar cal1 = Calendar.getInstance();
     cal1.add(Calendar.DAY_OF_MONTH,0);
	 
     Date date1 =  cal1.getTime();
	
     Calendar cal2 = Calendar.getInstance();
     cal2.add(Calendar.DAY_OF_MONTH,3);
	 
     Date date2 =  cal2.getTime();
	
   CriteriaBuilder cb = manager.getCriteriaBuilder();
   CriteriaQuery<Estadia> criteriaQuery = cb.createQuery(Estadia.class);
   Root<Estadia> root = criteriaQuery.from(Estadia.class);
   CriteriaQuery<Estadia> select = criteriaQuery.select(root);
   
  Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("dataSaida"), date1);
  Predicate endDatePredicate = cb.lessThanOrEqualTo(root.get("dataSaida"), date2);
  Predicate status = cb.equal(root.get("status"), true) ;
   
   
   Predicate and = cb.and(startDatePredicate,endDatePredicate,status);
   criteriaQuery.where(and);
   criteriaQuery.orderBy(cb.asc(root.get("dataSaida")));

   TypedQuery<Estadia> query = manager.createQuery(select);
   List<Estadia> list = query.getResultList();
   return list;

}

public boolean listarFOTO(){
	
	EntityManager manager = com.esl.SysDogN.util.JpaUtil.getEntityManager();

	 Calendar cal1 = Calendar.getInstance();
     cal1.add(Calendar.DAY_OF_MONTH,0);
	 
     Date date1 =  cal1.getTime();
	
     Calendar cal2 = Calendar.getInstance();
     cal2.add(Calendar.DAY_OF_MONTH,3);
	 
     Date date2 =  cal2.getTime();
	
   CriteriaBuilder cb = manager.getCriteriaBuilder();
   CriteriaQuery<Estadia> criteriaQuery = cb.createQuery(Estadia.class);
   Root<Estadia> root = criteriaQuery.from(Estadia.class);
   CriteriaQuery<Estadia> select = criteriaQuery.select(root);
   
  Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("dataSaida"), date1);
  Predicate endDatePredicate = cb.lessThanOrEqualTo(root.get("dataSaida"), date2);
  Predicate status = cb.equal(root.get("status"), true) ;
  Predicate foto = cb.equal(root.get("comFoto"), true) ; 
   
   Predicate and = cb.and(startDatePredicate,endDatePredicate,status,foto);
   criteriaQuery.where(and);
   criteriaQuery.orderBy(cb.asc(root.get("dataSaida")));

   TypedQuery<Estadia> query = manager.createQuery(select);
   Estadia y = query.getSingleResult();
   return y.getAnimal().getComFoto();

}





}

