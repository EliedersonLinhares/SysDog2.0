package com.esl.SysDogN.mb;

import static com.esl.SysDogN.util.FacesUtil.addDetailMessage;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.service.EstadiaService;
import com.esl.SysDogN.util.JpaUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Named
@ViewScoped
public class CadastroEstadiaMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private Estadia estadia = new Estadia();
	
	private Long CodigoEstadia;
	
	@Inject
	private EstadiaService estadiaService;
	
	// usado para edicao inciando se não for nulo
	public void inicializar() {
		if(CodigoEstadia != null) {
			estadia = estadiaService.porCodigo(CodigoEstadia);
		}
	}
	

	public String excluir() {
	
		estadiaService.delete(estadia);
		addDetailMessage("Estadia removido com sucesso");
        Faces.getFlash().setKeepMessages(true);
		return "lista-estadia.xhtml?faces-redirect=true";
	
	}	

	public Estadia getEstadia() {
		return estadia;
	}

	public void setEstadia(Estadia estadia) {
		this.estadia = estadia;
	}

	public Long getCodigoEstadia() {
		return CodigoEstadia;
	}

	public void setCodigoEstadia(Long codigoEstadia) {
		CodigoEstadia = codigoEstadia;
	}
	

	public void imprimir() throws IOException {
		
			try {
				
		     Long codigo = estadia.getCodigo();
		     
			//pegar o caminho do arquivo da memória
			String caminho = Faces.getRealPath("/relatorios/compestadia.jasper");

			Map<String, Object> parametros = new HashMap<>();//Guarda um nome e um valor, com uma estrutura de mapa
		
		    parametros.put("CODIGO_ESTADIA",codigo );//o valor ' %% ' indica que a tabela toda sera impresa
			
			Connection conexao = JpaUtil.getConexao();
			
			JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);//gera o relatorio populado
			
			LocalDateTime agora = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			String agoraFormatado = agora.format(formatter);
			
			String reportname = "estadia " + estadia.getCodigo() +" " + agoraFormatado;
			
			
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			  response.reset();
			  response.setContentType("application/pdf");
			  response.setHeader("Content-disposition", "attachment; filename=\""+ reportname +".pdf\"");
			  ServletOutputStream stream = response.getOutputStream();
			  JasperExportManager.exportReportToPdfStream(relatorio, stream);
			FacesContext.getCurrentInstance().responseComplete();
			
		}catch(JRException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um relatório");
			erro.printStackTrace();
		}
		}
	
	
	
}
