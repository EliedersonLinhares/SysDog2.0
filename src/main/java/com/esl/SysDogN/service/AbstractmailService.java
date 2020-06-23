package com.esl.SysDogN.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.omnifaces.util.Faces;

import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.model.ResetarSenha;
import com.esl.SysDogN.util.JpaUtil;
import com.ibm.icu.text.NumberFormat;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public abstract class AbstractmailService implements EmailService{
	
	@Override
	public void enviarEmailConclusaoEstadia(Estadia obj) throws EmailException, IOException, JRException {
		//SimpleEmail sm = prepareSimpleMailFromEstadia(obj);
		Email sm = prepareSmtpMailConclusaoEstadia(obj);
		//HtmlEmail sm = prepareHtmlMailConclusaoEstadia(obj);
		sendEmail(sm);
	}

	//mock
	protected SimpleEmail prepareSimpleMailFromEstadia(Estadia obj) throws EmailException, IOException {
	    Properties prop = getProp();
		
		SimpleEmail sm = new SimpleEmail();
		
	    sm.addTo(prop.getProperty("default.sender"));//Destinatario
	    sm.setFrom(prop.getProperty("default.sender"));//Remetente
	    sm.setSubject("Estadia Fechada. Codigo: " + obj.getCodigo());
	    sm.setMsg(obj.toString());
	    
	    return sm;
	}

	//email simples, somente texto
	protected Email prepareSmtpMailConclusaoEstadia(Estadia obj) throws EmailException, IOException {
	    Properties prop = getProp2();
		
	    SimpleEmail email = new SimpleEmail();
		   
		   email.setDebug(true);
		   
		   email.setHostName(prop.getProperty("mail.host")); // o servidor SMTP para envio do e-mail
		   
		   email.addTo(obj.getAnimal().getCliente().getEmail(), obj.getAnimal().getCliente().getNome()); //destinatário
		   
		   email.setFrom(prop.getProperty("default.sender"), prop.getProperty("default.sender.name")); // remetente
		   
		   email.setSubject("Estadia Fechada. Codigo: " + obj.getCodigo()); // assunto do e-mail
		   
		   email.setMsg(obj.toString()); //conteudo do e-mail
		   
		   email.setAuthentication(prop.getProperty("mail.username"),prop.getProperty("mail.password"));
		   
		   email.setSmtpPort(Integer.parseInt(prop.getProperty("mail.smtp.port")));
		  
		   email.setStartTLSEnabled(Boolean.parseBoolean(prop.getProperty("mail.smtp.setStartTLSEnabled")));
		   email.setSSLOnConnect(Boolean.parseBoolean(prop.getProperty("mail.smtp.ssl.enable")));
	
		 
		   return email;
	}
	
	
	//Pegar dados de arquivos properties
	 public static Properties getProp() throws IOException {
	        Properties props = new Properties();
	       String atualDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
	              +"WEB-INF/classes/application.properties";

	       FileInputStream file = new FileInputStream(atualDir);
	        props.load(file);
	        return props;
	 
	    }
	 
	 public static Properties getProp2() throws IOException {//testes junit
	        Properties props = new Properties();
	     
	        FileInputStream file = new FileInputStream(
	                "./src/main/resources/application.properties");
	        props.load(file);
	    
	        return props;
	 
	    }
	 
	 
	 
	 
	 @Override
	 public void enviarEmailHtmlConclusaoEstadia(Estadia obj) throws IOException, EmailException, JRException{
		HtmlEmail html = prepareHtmlMailConclusaoEstadia(obj);
	    sendHtmlEmail(html);
	 }
	 
	 
	 private HtmlEmail prepareHtmlMailConclusaoEstadia(Estadia obj) throws IOException, EmailException, JRException{
		 
		 HtmlEmail email = new HtmlEmail();
		 Properties prop = getProp2();
		 
		String atualDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
	              +"resources/images/logo276.png";
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); 
		 NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
		 
		 String image = email.embed(new File(atualDir));
		 
		 
		 String htmlEmailTemplate = " <html>"
		+ "<head>"
		+ "<title>Confirmação de Fechamento da Estadia</title>"
		+ "</head>"
		+ "<body>"
		+  "<div>"
		+ "<p>Olá, " + obj.getAnimal().getCliente().getNome() +"</p>"
		+ "<p>Estamos enviando esse email para confirmar o fechamento da estadia de seu animal conosco</p>"
		+ "<p>Estadia código: "+ obj.getCodigo() +"</p>"
		+ "<p>Nome do animal: " + obj.getAnimal().getNome() + "</p>"
		+ "<p>Data de entrada: " + sdf.format(obj.getDataEntrada()) + "</p>"
		+ "<p>Data de Saída: " + sdf.format(obj.getDataSaida()) + "</p>"
		+ "<p>Valor total: " + nf.format(obj.getValorCobrado()) +   "</p>"
		+ "<p>Atenção, este é um email enviado automaticamente pelo sistema, não responda este email pois ele não é monitorado</p>"
		+ "<p>Atenciosamente,</p>"
		+ "<img src=" + image + ">"
		+ "</div>"
		+ "</body>"
		+ "</html>";
		 
	       
		   email.setDebug(true);
		   
		   email.setHostName(prop.getProperty("mail.host"));
           email.addTo(obj.getAnimal().getCliente().getEmail(), obj.getAnimal().getCliente().getNome()); //destinatário
		   email.setFrom(prop.getProperty("default.sender"), prop.getProperty("default.sender.name")); // remetente
		   email.setSubject("Estadia Fechada. Codigo: " + obj.getCodigo()); // assunto do e-mail
           email.setAuthentication(prop.getProperty("mail.username"),prop.getProperty("mail.password"));
		   email.setSmtpPort(Integer.parseInt(prop.getProperty("mail.smtp.port")));
		   email.setStartTLSEnabled(Boolean.parseBoolean(prop.getProperty("mail.smtp.setStartTLSEnabled")));
		   email.setSSLOnConnect(Boolean.parseBoolean(prop.getProperty("mail.smtp.ssl.enable")));
		   
	       
	       email.setHtmlMsg(htmlEmailTemplate);

	        LocalDateTime agora = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			String agoraFormatado = agora.format(formatter);
			String reportname = "estadia " + obj.getCodigo() +" " + agoraFormatado;
	      
	      email.attach( new ByteArrayDataSource(gerarComprovantePdfArrayBytes(obj.getCodigo()), "application/pdf"), "", reportname);
		   return email;
		
}

	 /**
	  * Armazena o comprovante de fechamento da estadia em um array de bytes para ser enviado como
	  * anexo no email.
	 * @throws JRException 
	  */
	 public byte[] gerarComprovantePdfArrayBytes(Long codigo) throws JRException {
		 
		//pegar o caminho do arquivo da memória
			String caminho = Faces.getRealPath("/relatorios/compestadia.jasper");

			Map<String, Object> parametros = new HashMap<>();//Guarda um nome e um valor, com uma estrutura de mapa
		
		    parametros.put("CODIGO_ESTADIA",codigo );//o valor ' %% ' indica que a tabela toda sera impresa
			
			Connection conexao = JpaUtil.getConexao();
			
			JasperPrint relatorio;
		
			relatorio = JasperFillManager.fillReport(caminho, parametros, conexao);
			
			 byte[] pdfContend = JasperExportManager.exportReportToPdf(relatorio);
			 
			 return pdfContend;
		 
	 }
	 
	 @Override
	 public void enviarEmailHtmlAlterarSenha(ResetarSenha obj,String hash) throws IOException, EmailException {
		 HtmlEmail html = prepareHtmlMailAlterarSenha(obj,hash);
		 sendHtmlEmail(html);
	 }
	 
	 private HtmlEmail prepareHtmlMailAlterarSenha(ResetarSenha obj, String hash) throws IOException, EmailException{
		 
		 HtmlEmail email = new HtmlEmail();
		 Properties prop = getProp();
		 
		 String atualDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
	            +"resources/images/logo276.png";
		 String image = email.embed(new File(atualDir));
		
		 
		 
		 String htmlEmailTemplate = " <html>"
					+ "<head>"
					+ "<title>Confirmação de Fechamento da Estadia</title>"
					+ "</head>"
					+ "<body>"
					+  "<div>"
					+ "<p>Estamos enviando esse email para confirmar a alteração de senha, para prosseguir copie e cole na parte de confirmação de senha o codigo abaixo.</p>"
					+ "<p>"+ hash +"</p>"
					+ "<p>Se não foi voce que efetuou o pedido de alteração, é recomendado que altere sua senha</p>"
					+ "<p>Atenção, este é um email enviado automaticamente pelo sistema, não responda este email pois ele não é monitorado</p>"
					+ "<p>Atenciosamente,</p>"
					+ "<img src=" + image + ">"
					+ "</div>"
					+ "</body>"
					+ "</html>";
		 
		 
		   email.setDebug(true);
		   
		   email.setHostName(prop.getProperty("mail.host"));
           email.addTo(obj.getUsuario().getEmail(), obj.getUsuario().getNome()); //destinatário
		   email.setFrom(prop.getProperty("default.sender"), prop.getProperty("default.sender.name")); // remetente
		   email.setSubject("Pedido de alteração de senha"); // assunto do e-mail
           email.setAuthentication(prop.getProperty("mail.username"),prop.getProperty("mail.password"));
		   email.setSmtpPort(Integer.parseInt(prop.getProperty("mail.smtp.port")));
		   email.setStartTLSEnabled(Boolean.parseBoolean(prop.getProperty("mail.smtp.setStartTLSEnabled")));
		   email.setSSLOnConnect(Boolean.parseBoolean(prop.getProperty("mail.smtp.ssl.enable")));
		   
	       
	       email.setHtmlMsg(htmlEmailTemplate);
		 
		 return email;
	 }
 
	 /**
	 private HtmlEmail prepareHtmlMailAlterarSenha(ResetarSenha obj, String link) throws IOException, EmailException{
		 
		 HtmlEmail email = new HtmlEmail();
		 Properties prop = getProp();
		 
		 String atualDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
	            +"resources/images/logo276.png";
		 String image = email.embed(new File(atualDir));
		 
		 String partlink = "http://localhost:8080/SysDogN/confirmarsenha.jsf";
		 
		 
		 String htmlEmailTemplate = " <html>"
					+ "<head>"
					+ "<title>Confirmação de Alteração de Senha/title>"
					+ "</head>"
					+ "<body>"
					+  "<div>"
					+ "<p>Olá, " + obj.getUsuario().getNome() +"</p>"
					+ "<p>Estamos enviando esse email para confirmar a alteração de senha, para proceguir clique no link abaixo: ou copie e cole no seu navegador.</p>"
					+ "<p> <a href="+ partlink +"?hash="+ link +">Link para a confirmação</a></p>"
					+ "<p>Ou copie e cole no seu navegador:</p>"
					+ "<p>"+ partlink +"?hash="+ link +"</p>"
				    + "<p>Se não foi voce que efetuou o pedido de alteração, é recomendado que altere sua senha</p>"
					+ "<p>Atenção, este é um email enviado automaticamente pelo sistema, não responda este email pois ele não é monitorado</p>"
					+ "<p>Atenciosamente,</p>"
			      	+ "<img src=" + image + ">"
					+ "</div>"
					+ "</body>"
					+ "</html>";
		 
		 
		   email.setDebug(true);
		   
		   email.setHostName(prop.getProperty("mail.host"));
           email.addTo(obj.getUsuario().getEmail(), obj.getUsuario().getNome()); //destinatário
		   email.setFrom(prop.getProperty("default.sender"), prop.getProperty("default.sender.name")); // remetente
		   email.setSubject("Pedido de alteração de senha"); // assunto do e-mail
           email.setAuthentication(prop.getProperty("mail.username"),prop.getProperty("mail.password"));
		   email.setSmtpPort(Integer.parseInt(prop.getProperty("mail.smtp.port")));
		   email.setStartTLSEnabled(Boolean.parseBoolean(prop.getProperty("mail.smtp.setStartTLSEnabled")));
		   email.setSSLOnConnect(Boolean.parseBoolean(prop.getProperty("mail.smtp.ssl.enable")));
		   
	       
	       email.setHtmlMsg(htmlEmailTemplate);
		 
		 return email;
	 }
	 */
	 
}
