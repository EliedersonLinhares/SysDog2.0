package com.esl.SysDogN.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.esl.SysDogN.util.JpaUtil;



public class DashboardDAO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	public static BigDecimal TotalEstadiaMensal(Date date1 , Date date2) {
		
		EntityManager manager = JpaUtil.getEntityManager();
		
		TypedQuery<BigDecimal> query = manager.createQuery(
				"select SUM(u.total) from Estadia u where u.dataEntrada>= :date1 AND u.dataEntrada<= :date2",
				BigDecimal.class);
		 query.setParameter("date1", date1, TemporalType.TIMESTAMP);
	       query.setParameter("date2", date2, TemporalType.TIMESTAMP);
		return query.getSingleResult();
		
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
}