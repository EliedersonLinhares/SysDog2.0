package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import com.esl.SysDogN.model.Animal;
import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.model.Usuario;
import com.esl.SysDogN.service.AnimalService;
import com.esl.SysDogN.service.EstadiaService;
import com.esl.SysDogN.service.SmtpMailService;
import com.esl.SysDogN.service.UsuarioService;
import com.esl.SysDogN.util.JpaUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;



@ManagedBean
@Named
@ViewScoped
public class CalendarioEstadiaMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstadiaService estadiaService;
	
	
	private Estadia estadia = new Estadia();
	private List<Estadia> estadias;
	
    private LazyDataModel<Estadia> dataModel;
    
    private Usuario usuarioAtual;

    private Long CodigoEstadia;
	
	public Long getCodigoEstadia() {
		return CodigoEstadia;
	}

	public void setCodigoEstadia(Long codigoEstadia) {
		CodigoEstadia = codigoEstadia;
	}

	public Usuario getUsuarioAtual() {
		return usuarioAtual;
	}

	public void setUsuarioAtual(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}
    
    
	
    public List<SelectItem> getAnimais(){
    //	EstadiaService estadiaService = new EstadiaService();
    	
		return estadiaService.getListAnimal();
	}
    
    public List<SelectItem> getAnimaisDisponiveis(){
        //	EstadiaService estadiaService = new EstadiaService();
    		return estadiaService.getListAnimalDisponivel();
    	}
    
    
    
    public List<SelectItem> getLocais(){
    //	EstadiaService estadiaService = new EstadiaService();
		return estadiaService.getListLocal();
	}

	public LazyDataModel<Estadia> getDataModel() {
		return dataModel;
	}
	
	private ScheduleModel eventModel;
	
	 private LazyScheduleModel lazyEventModel;
	 
	 
	 public Estadia getEstadia() {
			return estadia;
		}
		public void setEstadia(Estadia estadia) {
			this.estadia = estadia;
		}
		public List<Estadia> getEstadias() {
			return estadias;
		}
		public void setEstadias(List<Estadia> estadias) {
			this.estadias = estadias;
		}
	 
	 
	 
	 
	 public ScheduleModel getLazyEventModel() {
	        return lazyEventModel;
	    }
	 
	 public ScheduleModel getEventModel() {
			return eventModel;
		}

		public void setEventModel(ScheduleModel eventModel) {
			this.eventModel = eventModel;
		}
		
	
		
		
		
		@SuppressWarnings("serial")
		@PostConstruct
	    public void init() {
			buscarUsuarioAtual();
		   lazyEventModel = new LazyScheduleModel() {
			

				@Override
	            public void loadEvents(Date start, Date end) {
		   
	            	List<DefaultScheduleEvent> scheduleEvents =
	            	          getEventos(start,end);
	            	for (DefaultScheduleEvent defaultScheduleEvent : scheduleEvents) {
	                    addEvent(defaultScheduleEvent);
	            	}
	            	
	            	
	            	
	            	//eventModel = new DefaultScheduleModel();//Carrega o modelo do schedule
	        estadia = new Estadia();//nova estadia
	        
	       
	            }   
	        
	        
	    };
		   }	 
		
		
		
		private List<DefaultScheduleEvent> getEventos(Date dataInicial, Date dataFinal) {
			   
			  
			   EstadiaService estadiaService2 = new EstadiaService();

			   /*
			    * Seta data de exibição somente para cadastros efetuados 2 meses antes e 2 meses após a 
			    * data atual 
			    */
			     LocalDateTime hoje = LocalDateTime.now();
			    
			     LocalDateTime  ldt1 = hoje.minusMonths(4).with(TemporalAdjusters.lastDayOfMonth());	
				 Date dateI = Date.from(ldt1.atZone(ZoneId.systemDefault()).toInstant());
				
				LocalDateTime ldt2 = hoje.plusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
				Date dateF = Date.from(ldt2.atZone(ZoneId.systemDefault()).toInstant());
			   
			
			   try {
				  estadias = estadiaService2.findDates(dateI, dateF);
				    } catch (Exception e) {
				      e.printStackTrace();
				    }
			   
			   
			   List<DefaultScheduleEvent> eventos = new ArrayList<DefaultScheduleEvent>();
			   
			   
			   for(Estadia ev : estadias) {//for para precher o calendario com o conteudo de estadia 
		        	DefaultScheduleEvent evento = new DefaultScheduleEvent( );//novo evento
		        	evento.setEndDate(ev.getDataSaida());
		        	evento.setStartDate(ev.getDataEntrada());
		            evento.setTitle(" - Estadia: "+ ev.getAnimal().getNome());
		        	evento.setData(ev.getCodigo());
		        	evento.setDescription(ev.getDescricao());
		        	evento.setAllDay(false);
		        	evento.setEditable(false);
		         
		        	//altera a cor do evento de acordo com o status, com base em valor no arquivo css,
		        	if(ev.getStatus() == true) {
		        	evento.setStyleClass("event1");	
		        	}else if(ev.getStatus() == false) {
		        		evento.setStyleClass("event2");
		        	}
		        	lazyEventModel.addEvent(evento);
		        	
		        	
			        
		        }
			   
			   return eventos;
		   }
		   
		 public void selecionado(SelectEvent selectEvent) {//mostrar os dados de uma estadia especifica
			 
			   ScheduleEvent auxEvent = (ScheduleEvent) selectEvent.getObject();
			   for(Estadia ev : estadias) {//for para mostrar o dados da estadia selecionada
				   if(ev.getCodigo() == (Long)auxEvent.getData()) {//compara o id da tabela com id do schedule
					   estadia = ev;
					   break;
				   }
			   }
		   }
		 
		   public void Novo(SelectEvent selectEvent) {//preencher os campos das datas quando clicado em uma data especifica
			   ScheduleEvent event = new DefaultScheduleEvent("",(Date)selectEvent.getObject(),(Date)selectEvent.getObject());//criando um novo schedulEvent com os campos de inicio e fim
			   estadia = new Estadia();
			   estadia.setDataEntrada(new java.sql.Date(event.getStartDate().getTime()));//recupera a data clicada usando of formato que o schedule entenda, passando para o campo
			   estadia.setDataSaida(new java.sql.Date(event.getStartDate().getTime()));
			   
			   atribuirValores();
	        }

		   public void abriEstadia() {
			   
			  
			   
			   EstadiaService estadiaService2 = new EstadiaService();
			   AnimalService animalService2 = new AnimalService();  
				 
				   Animal animal = animalService2.buscaporCodigo(estadia.getAnimal().getCodigo());
			
				   final BigDecimal valor = new BigDecimal("0.0");
				   if(estadia.getTotal().compareTo(valor) <= 0){
					   Faces.getFlash().setKeepMessages(true);
			           Messages.addError(null,"O valor total da estadia não pode ser negativo ou zero!");
			           return;
				   }
				   
					   if(estadia.getDataEntrada().getTime() <= estadia.getDataSaida().getTime()) {//verifica se a data de saida não é anterior a data de entrada
						
						   try {
							   estadia.setStatus(true);
							   estadia.setUsuario(getUsuarioAtual());
							   estadiaService2.merge(estadia);
							   
							  
							   animal.setAlocado(true);
							   animalService2.merge(animal);
							   
							   
							   Faces.getFlash().setKeepMessages(true);
					           Messages.addInfo(null,"Estadia salva com sucesso!");

						   }catch(Exception erro) {
							   
							   Faces.getFlash().setKeepMessages(true);
					           Messages.addError(null,"Ocorreu um erro ao tentar salvar a estadia!");
								erro.printStackTrace();
						   }
						   estadia = new Estadia();
					   }else {
						   Faces.getFlash().setKeepMessages(true);
				           Messages.addError(null,"A data entrada não pode ser anterior a data de saida!");
						  
					   }
			
		   }
		   
		   
		   
		   
		   
		   
      public void fecharEstadia() {
			   
			   EstadiaService estadiaService2 = new EstadiaService();
			   AnimalService animalService2 = new AnimalService();  
			   
			 
					 
					 Animal animal = animalService2.buscaporCodigo(estadia.getAnimal().getCodigo());//pega o codigo do animal escolhido para hospedar
					   
					   if(estadia.getDataEntrada().getTime() <= estadia.getDataSaida().getTime()) {//verifica se a data de saida não é anterior a data de entrada
						   
						   try {
							   
							   estadia.setStatus(false);
							   estadiaService2.merge(estadia);
							  
							
							   animal.setAlocado(false);
							   animalService2.merge(animal);
					
							   Faces.getFlash().setKeepMessages(true);
					           Messages.addInfo(null,"Estadia concluida com sucesso!");
							  
					          // MockMailService mock = new MockMailService();
					          // mock.enviarEmailConclusaoEstadia(estadia);
					           
					           SmtpMailService smtp = new SmtpMailService();
					           smtp.enviarEmailHtmlConclusaoEstadia(estadia);   
					           
						
						   }catch(Exception erro) {
							   Faces.getFlash().setKeepMessages(true);
					           Messages.addError(null,"Ocorreu um erro ao tentar concluir a estadia!");
								erro.printStackTrace();
						   }
						   
					   }else {
						   Faces.getFlash().setKeepMessages(true);
				           Messages.addError(null,"A data entrada não pode ser anterior a data de saida!");
							
					   }
				   }
     
     
     
	
      
      
      
      
      public void AlterarEstadia() {
		   
		   EstadiaService estadiaService2 = new EstadiaService();

				   if(estadia.getDataEntrada().getTime() <= estadia.getDataSaida().getTime()) {//verifica se a data de saida não é anterior a data de entrada
					   
					   try {
						   estadiaService2.merge(estadia);
			
						   Faces.getFlash().setKeepMessages(true);
				           Messages.addInfo(null,"Estadia alterada com sucesso!");
					
					   }catch(Exception erro) {
						   Faces.getFlash().setKeepMessages(true);
				           Messages.addError(null,"Ocorreu um erro ao tentar alterar a estadia!");
							erro.printStackTrace();
					   }
					   
				   }else {
					   Faces.getFlash().setKeepMessages(true);
			           Messages.addError(null,"A data entrada não pode ser anterior a data de saida!");
						
				   }
			   }
      
      
     //Usado com o ajax para automaticamente preencher o campo total 
    public void somaValores() {
    	estadia.setTotal(estadia.getValorCobrado().add(estadia.getGastoOutros()).add(estadia.getGastoRacao()).add(estadia.getGastoVacina()).subtract(estadia.getDesconto()));
    }
      
   public void atribuirValores() {
	 //inicia com os valores zerados para evitar erro de nullpointer ao usar o ajax
	   estadia.setGastoOutros(new BigDecimal(0.00));
	   estadia.setGastoRacao(new BigDecimal(0.00));
	   estadia.setGastoVacina(new BigDecimal(0.00));
	   estadia.setDesconto(new BigDecimal(0.00));
	   estadia.setValorCobrado(new BigDecimal(0.00));
	   estadia.setTotal(new BigDecimal(0.00));
   }

    public void buscarUsuarioAtual() {
				AutenticaMB  aut = new AutenticaMB();
				String email = aut.getCurrentUser();
				UsuarioService usuarioService = new UsuarioService();
				usuarioAtual = usuarioService.buscaporEmail(email);
			}
		   
     public String excluir() {
				
				estadiaService.delete(estadia);
				addDetailMessage("Estadia removida com sucesso");
		        Faces.getFlash().setKeepMessages(true);
				return "historico-estadia.xhtml?faces-redirect=true";
	}	   
		   
}
