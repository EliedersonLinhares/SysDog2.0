package com.esl.SysDogN.teste.S3;

import java.util.List;

import org.junit.Test;

import com.esl.SysDogN.mb.DashboardMB;
import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.service.DashboardService;

public class DashboardTest {
	Estadia estadia = new Estadia();
	DashboardService mb = new DashboardService();
	DashboardMB mb2 = new DashboardMB();
	
	@Test
	public void testDatView() {
		
		List<Estadia> x = mb.listarEstadiaD();
		
		 for(Estadia estadia : x) {
			System.out.println("Nome: " + estadia.getAnimal().getNome());
		     if(estadia.getAnimal().getComFoto()) {
		    	 System.out.println(mb.listarFOTO());
		     }else {
		     System.out.println(mb.listarFOTO());
		 }
		 }	 
	}

}
