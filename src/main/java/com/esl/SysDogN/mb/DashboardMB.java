package com.esl.SysDogN.mb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;

import com.esl.SysDogN.model.Despesa;
import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.service.DashboardService;


@Named
@ViewScoped
public class DashboardMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DashboardService dashboardService;
	
	private List<Estadia> estadias;
	
	private Estadia estadia;
	
	 public Estadia getEstadia() {
		return estadia;
	}


	public void setEstadia(Estadia estadia) {
		this.estadia = estadia;
	}


	private DonutChartModel donutModel;
	  
	  private BarChartModel barModel2;
	  
	  private LazyDataModel<Despesa> dataModel;
		
		public LazyDataModel<Despesa> getDataModel() {
			return dataModel;
		}
	  
	  
	  public BarChartModel getBarModel2() {
	        return barModel2;
	    }
	 
	    public void setBarModel2(BarChartModel barModel2) {
	        this.barModel2 = barModel2;
	    }
	    public DonutChartModel getDonutModel() {
	        return donutModel;
	    }
	 
	    public void setDonutModel(DonutChartModel donutModel) {
	        this.donutModel = donutModel;
	    }

	
	    @PostConstruct
	    public void init() {
	      
	       estadias = dashboardService.listarEstadiaD();
	        createBarModel2();
	        createDonutModel();
	   
	    }
	    
	    public DashboardMB() {
            dataModel = new LazyDataModel<Despesa>() {

				private static final long serialVersionUID = 1L;
				
				 @Override
				  public List<Despesa> load(int first, int pageSize, String sortField,
				                             SortOrder sortOrder, Map<String, Object> filters) {
					 
					 this.setRowCount( dashboardService.getTotalRegistrosDespesaD().intValue());
				     
					 List<Despesa> list =  dashboardService.listarLazyDespesaD(first, pageSize, filters,sortField, sortOrder);
				      if (filters != null && filters.size() > 0) {
				          this.setRowCount( dashboardService.getColunasFiltradasDespesaD(filters));
				      }
				      return list;
				  }
				
			};
	  }
	
	
	    
	    
	    
	public BigDecimal TotaMensal() {
		
		
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.HOUR_OF_DAY, 0);
	    cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
       
        
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
         
		
        Date date1 =  cal1.getTime();
		Date date2 = cal2.getTime();

		BigDecimal resultado = dashboardService.TotalEstadiaMensal(date1, date2);
		
		if (resultado == null) {
			return (new BigDecimal(0));
		}
		   
		
		return resultado;
	}
	
	public String MêsAtual() {
		LocalDate mes = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
		String mesAtual =  mes.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt"));
		String anoAtual = Integer.toString( mes.getYear());
		
		return "(" + mesAtual + " - " + anoAtual + ")";
	}
	public String DiaAtual() {
		LocalDate dia = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
		return dia.toString();
	}
	public String Amanha() {
		LocalDate dia = LocalDate.now(ZoneId.of("America/Sao_Paulo")).plusDays(1);
		return dia.toString();
	}
	public String DiaHoraAtual() {
		LocalDateTime dia = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
		return dia.toString();
	}
	public String DiaHoraAmanha() {
		LocalDateTime dia = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusDays(1);
		return dia.toString();
	}
	
	
	
   public BigDecimal TotaDespesa() {
		
	
		
	   Calendar cal1 = Calendar.getInstance();
	   cal1.set(Calendar.HOUR_OF_DAY, 0);
	   cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
       
        
        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.MINUTE, 0);
         cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
         
		
        Date date1 =  cal1.getTime();
		Date date2 = cal2.getTime();

		BigDecimal resultado = dashboardService.TotalDespesaMensal(date1, date2);
		
		if (resultado == null) {
			return (new BigDecimal(0));
		}
		   
		
		return resultado;
	}
   
   public BigDecimal TotalCaixa() {
   	
	   BigDecimal resultado =   TotaMensal().subtract(TotaDespesa());   
   	
	   return resultado;
   }
   
   ///////////////
   public String CorExibicaoResultado() {
	   String color;
	   
	   if((TotalCaixa()).intValue() >= 0){
		color = "color-blue";
		   return  color;   
	   }else {
		color = "color-red";
		   return  color; 
	   }
   }
   
 

   
   public List<Estadia> estadiasD(){
	List<Estadia>  estadiasD = dashboardService.listarEstadiaD();
	return estadiasD;
   }
   
   
   
   public int EstadiasAbertas() {
	   
	   Calendar cal1 = Calendar.getInstance();
	   cal1.set(Calendar.HOUR_OF_DAY, 0);
	   cal1.set(Calendar.MINUTE, 0);
       cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
       
       Calendar cal2 = Calendar.getInstance();
       cal2.set(Calendar.HOUR_OF_DAY, 0);
	   cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
        
		
       Date date1 =  cal1.getTime();
	   Date date2 = cal2.getTime();
 
	   int resultado = dashboardService.EstadiasAbertas(date1,date2);
	   return resultado; 
  }
   

   
  /**
   * Lista que retorna os 6 meses anteriores incluindo o atual para uso no grafico
   */
	 public List<String> ListaM(){
         List<String> lista = new ArrayList<String>();
         for(int i=0; i <= 6; i++) {
				LocalDate mes = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
				LocalDate  mes2 = mes.minusMonths(i);
				String mesAtual =  mes2.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt"));
				String anoAtual = Integer.toString( mes2.getYear());
				lista.add(mesAtual +" - " + anoAtual);
         }
         
         return lista;
         
     }
	 
	 
/**
 * 
 * Lista que retorna a soma mensal das despesas dos 6 meses anterioes incluindo o atual
 */
 public List<BigDecimal> Despesas(){
		 
		 List<BigDecimal> lista = new ArrayList<BigDecimal>();
		 
		 for(int i=-6; i <= 0 ; i++)  {
				 Calendar cal1 = Calendar.getInstance();
				   cal1.add(Calendar.MONTH, i);
				   cal1.set(Calendar.HOUR_OF_DAY, 0);
				   cal1.set(Calendar.MINUTE, 0);
			       cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
			       
			       Calendar cal2 = Calendar.getInstance();
			       cal2.add(Calendar.MONTH, i);
			       cal2.set(Calendar.HOUR_OF_DAY, 0);
				   cal2.set(Calendar.MINUTE, 0);
			        cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
			       
					
			       Date date1 =  cal1.getTime();
				   Date date2 = cal2.getTime();
				   BigDecimal resultado = dashboardService.TotalDespesaMensal(date1, date2);
				   
				 
				   
				   if (resultado == null) {
						resultado = (new BigDecimal(0));
					}
				   
				   lista.add(resultado);
				
		}
		 return lista;
	 }
 
 /**
  * 
  * Lista que retorna a soma mensal das estadias dos 6 meses anterioes incluindo o atual
  */
 public List<BigDecimal> Estadias(){
	 
	 List<BigDecimal> lista = new ArrayList<BigDecimal>();
	 
	 for(int i=-6; i <= 0 ; i++) {
			 Calendar cal1 = Calendar.getInstance();
			   cal1.add(Calendar.MONTH, i);
			   cal1.set(Calendar.HOUR_OF_DAY, 0);
			   cal1.set(Calendar.MINUTE, 0);
		       cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
		       
		       Calendar cal2 = Calendar.getInstance();
		       cal2.add(Calendar.MONTH, i);
		       cal2.set(Calendar.HOUR_OF_DAY, 0);
			   cal2.set(Calendar.MINUTE, 0);
		        cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
		       
				
		       Date date1 =  cal1.getTime();
			   Date date2 = cal2.getTime();
			   BigDecimal resultado = dashboardService.TotalEstadiaMensal(date1, date2);
			   
			 
			   
			   if (resultado == null) {
					resultado = (new BigDecimal(0));
				}
			   
			   lista.add(resultado);
			
	}
	 return lista;
 }
   
   
   
   public void createBarModel2() {
       barModel2 = new BarChartModel();
       ChartData data = new ChartData();
        
       BarChartDataSet barDataSet = new BarChartDataSet();
       barDataSet.setLabel("Arrecadação");
       barDataSet.setBackgroundColor("rgba(60, 141, 188, 0.2)");//Cor primaria com nivel de transparencia
       barDataSet.setBorderColor("rgb(60, 141, 188)");//cor solida na borda
       barDataSet.setBorderWidth(1);
       List<Number> values = new ArrayList<>();
       
       for(int i=0; i <= 6; i++) {
    	   values.add(Estadias().get(i));
       }
      
       barDataSet.setData(values);
        
       BarChartDataSet barDataSet2 = new BarChartDataSet();
       barDataSet2.setLabel("Despesas");
       barDataSet2.setBackgroundColor("rgba(255, 99, 132, 0.2)");//Cor vermelha com nivell de transparencia
       barDataSet2.setBorderColor("rgb(255, 99, 132)");//cor solida na borda
       barDataSet2.setBorderWidth(1);
       List<Number> values2 = new ArrayList<>();
      
       
       for(int i=0; i <= 6; i++) {
    	   values2.add(Despesas().get(i));
       }
       barDataSet2.setData(values2);

       data.addChartDataSet(barDataSet);
       data.addChartDataSet(barDataSet2);
        
       List<String> labels = new ArrayList<>();
       for(int i=6; i >= 0; i--) {
    	   labels.add(ListaM().get(i));
       }
       data.setLabels(labels);
       barModel2.setData(data);
        
       //Options
       BarChartOptions options = new BarChartOptions();
       CartesianScales cScales = new CartesianScales();
       CartesianLinearAxes linearAxes = new CartesianLinearAxes();
       CartesianLinearTicks ticks = new CartesianLinearTicks();
       ticks.setBeginAtZero(true);
       linearAxes.setTicks(ticks);
       cScales.addYAxesData(linearAxes);
       options.setScales(cScales);
        
       Title title = new Title();
       title.setDisplay(true);
       title.setText("Resultado financeiro dos utimos seis meses");
       options.setTitle(title);
        
       barModel2.setOptions(options);
   }
   
   
   public List<BigDecimal> DespesasporTipo(){
	   Calendar cal1 = Calendar.getInstance();
	   cal1.add(Calendar.MONTH, -6);
	   cal1.set(Calendar.HOUR_OF_DAY, 0);
	   cal1.set(Calendar.MINUTE, 0);
       cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
       
       Calendar cal2 = Calendar.getInstance();
       cal2.add(Calendar.MONTH, 0);
       cal2.set(Calendar.HOUR_OF_DAY, 0);
	   cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
       
		
       Date date1 =  cal1.getTime();
	   Date date2 = cal2.getTime();
	   
	   List<BigDecimal> somas = new ArrayList<BigDecimal>();
	   List<String> listatipo = new ArrayList<String>();
	   
	   listatipo.add("Taxas de Propriedade(luz,Água,Gás)");
	   listatipo.add("Telefone e Internet");
	   listatipo.add("Materiais de Limpeza");
	   listatipo.add("Materiais de Escritório");
	   listatipo.add("Seguros e Sistemas de segurança");
	   listatipo.add("Legais e Jurídico");
	   listatipo.add("Pró Labore");
	   listatipo.add("Salário");
	   listatipo.add("Aluguel");
	   listatipo.add("Outros");
	   
	   for(int i=0; i<=9; i++) {

	   BigDecimal resultado = dashboardService.TotalDespesaMensalTipo(date1, date2, listatipo.get(i));
	   if (resultado == null) {
			resultado = (new BigDecimal(0));
		}
	   somas.add(resultado);
	   }
	   
	   return somas;
   }
   
   
   
   
   public void createDonutModel() {
       donutModel = new DonutChartModel();
       ChartData data = new ChartData();
        
       DonutChartDataSet dataSet = new DonutChartDataSet();
       List<Number> values = new ArrayList<>();
       for(int i=0; i<=9; i++) {
    	   values.add(DespesasporTipo().get(i));
       }
  
       dataSet.setData(values);
        
       List<String> bgColors = new ArrayList<>();
       bgColors.add("rgba(255, 99, 132,0.6)");//vermelho
       bgColors.add("rgba(54, 162, 235,0.6)");//amarelo
       bgColors.add("rgba(255, 205, 86,0.6)");//azul
       
       bgColors.add("rgba(112, 168, 59,0.6)");//verde
       bgColors.add("rgba(219, 157, 161,0.6)");//rosa
       bgColors.add("rgba(132, 89, 60,0.6)");//marron
       
       bgColors.add("rgba(158, 158, 158,0.6)");//cinza
       bgColors.add("rgba(229, 180, 99,0.6)");//laranja
       bgColors.add("rgba(162, 207, 240,0.6)");//azul claro
       
       bgColors.add("rgba(52, 52, 52,0.6)");//preto
     
       dataSet.setBackgroundColor(bgColors);
       
       List<String> boderColors = new ArrayList<>();
       
       boderColors.add("rgb(255, 99, 132)");//vermelho
       boderColors.add("rgb(54, 162, 235)");//amarelo
       boderColors.add("rgb(255, 205, 86)");//azul
       
       boderColors.add("rgb(112, 168, 59)");//verde
       boderColors.add("rgb(219, 157, 161)");//rosa
       boderColors.add("rgb(132, 89, 60)");//marron
       
       boderColors.add("rgb(158, 158, 158)");//cinza
       boderColors.add("rgb(229, 180, 99)");//laranja
       boderColors.add("rgb(162, 207, 240)");//azul claro
       
       boderColors.add("rgb(52, 52, 52)");//preto
       
       
       dataSet.setBorderColor(boderColors);
        
       data.addChartDataSet(dataSet);
       List<String> labels = new ArrayList<>();
       labels.add("Taxas de Propriedade(luz,Água,Gás)");
       labels.add("Telefone e Internet");
       labels.add("Materiais de Limpeza");
       labels.add("Materiais de Escritório");
       labels.add("Seguros e Sistemas de segurança");
       labels.add("Legais e Jurídico");
       labels.add("Pró Labore");
       labels.add("Salário");
       labels.add("Aluguel");
       labels.add("Outros");
       data.setLabels(labels);
  
       donutModel.setData(data);
   }
   
public List<Estadia> getEstadias() {
	return estadias;
}


public void setEstadias(List<Estadia> estadias) {
	this.estadias = estadias;
}


 
}