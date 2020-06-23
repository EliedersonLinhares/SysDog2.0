package com.esl.SysDogN.service;

import java.io.IOException;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;

import com.esl.SysDogN.model.Estadia;
import com.esl.SysDogN.model.ResetarSenha;

import net.sf.jasperreports.engine.JRException;

public interface EmailService {
	
	void enviarEmailConclusaoEstadia(Estadia obj) throws EmailException, IOException, JRException;
	
	void sendEmail(Email sm) throws EmailException;
	
	
	void enviarEmailHtmlConclusaoEstadia(Estadia obj) throws EmailException, IOException, JRException;
	
	void sendHtmlEmail(HtmlEmail html) throws EmailException;
	
	
	void enviarEmailHtmlAlterarSenha(ResetarSenha obj, String hash) throws EmailException, IOException;
	


}
