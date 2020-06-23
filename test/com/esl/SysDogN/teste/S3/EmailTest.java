package com.esl.SysDogN.teste.S3;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Ignore;
import org.junit.Test;

import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.service.EstadiaService;
import com.esl.SysDogN.service.MockMailService;
import com.esl.SysDogN.service.SmtpMailService;
import com.sun.mail.smtp.SMTPTransport;

import net.sf.jasperreports.engine.JRException;


public class EmailTest {

	// for example, smtp.mailgun.org
    private static final String SMTP_SERVER = "smtp.gmail.com ";
    private static final String USERNAME = "eliederson250@gmail.com";
    private static final String PASSWORD = "overdrive2103";

    private static final String EMAIL_FROM = "eliederson250@gmail.com";
    private static final String EMAIL_TO = "eliederson210@outlook.com";
    private static final String EMAIL_TO_CC = "";

    private static final String EMAIL_SUBJECT = "Test Send Email via SMTP";
    private static final String EMAIL_TEXT = "Hello Java Mail \n ABC123";
	
	
	@Test
	@Ignore
    public void sendmail2() {
 	

        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587"); // default port 25

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {
		
			// from
            msg.setFrom(new InternetAddress(EMAIL_FROM));

			// to 
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));

			// cc
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(EMAIL_TO_CC, false));

			// subject
            msg.setSubject(EMAIL_SUBJECT);
			
			// content 
            msg.setText(EMAIL_TEXT);
			
            msg.setSentDate(new Date());

			// Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
			// connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
			
			// send
            t.sendMessage(msg, msg.getAllRecipients());

            
            
            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
    
    
    
    
    
    
	
   @Test
   @Ignore
	public void sendmail () throws EmailException {
	   
	   Email  sm = new SimpleEmail();
		
		 sm.setDebug(true);
		 
		 System.out.println("Enviando email..");
		sm.setHostName("smtp.gmail.com");
		sm.setSmtpPort(465);
		
		sm.setFrom("eliederson250@gmail.com", "eliederson");//Remetente
		sm.setSubject("Teste de email");
		sm.setMsg("Teste do envio de email pelo commons mail");
	    sm.addTo("eliederson210@outlook.com");//Destinatario
	    
	    System.out.println("Autenticando...");
	    sm.setAuthentication("eliederson250@gmail.com", "overdrive2103");
	   sm.setStartTLSEnabled(true);
	    //sm.setAuthenticator(new DefaultAuthenticator("eliederson250@gmail.com", "overdrive2103"));
	    
		sm.setSSLOnConnect(true);
	    
	    sm.send();
	    
	    System.out.println("email enviado");
	}
   
  
   @Test//funcionando
   @Ignore
   public void sendmail3 () throws EmailException {
	   
	   SimpleEmail email = new SimpleEmail();
	   
	   email.setDebug(true);
	   email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
	   email.addTo("eliederson210@outlook.com", "Eliederson"); //destinatário
	   email.setFrom("eliederson250@gmail.com", "ELS"); // remetente
	   email.setSubject("Teste -> Email simples"); // assunto do e-mail
	   email.setMsg("Teste de Email utilizando commons-email 2"); //conteudo do e-mail
	   email.setAuthentication("eliederson250@gmail.com", "overdrive2103");
	   email.setSmtpPort(465);
	  
	   email.setStartTLSEnabled(true);
	   email.setSSLOnConnect(true);
	   // email.setSSL(true);
	  // email.setTLS(true);
	   email.send();
   }
   
   @Test
   @Ignore
   public void sendmailEstadia() throws EmailException, IOException, JRException {
	   
	   EstadiaService estadiaService= new EstadiaService();
	   Estadia estadia = estadiaService.buscaporCodigo(49l);
	   
	   SmtpMailService smtp = new SmtpMailService();
       smtp.enviarEmailConclusaoEstadia(estadia);   
	   
   }
   
   @Test
   
   public void sendHtmlmailEstadia() throws EmailException, IOException, JRException {
	   
	   EstadiaService estadiaService= new EstadiaService();
	   Estadia estadia = estadiaService.buscaporCodigo(51l);
	   
	   
	  // MockMailService mock = new MockMailService();
	   //mock.enviarEmailHtmlConclusaoEstadia(estadia);
	   
	   
	   SmtpMailService smtp = new SmtpMailService();
       smtp.enviarEmailHtmlConclusaoEstadia(estadia);   
	      
	   
   }
   
}
