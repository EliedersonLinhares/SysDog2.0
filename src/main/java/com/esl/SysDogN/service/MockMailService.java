package com.esl.SysDogN.service;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import com.esl.SysDogN.model.ResetarSenha;


public class MockMailService extends AbstractmailService {
	
	@Override
	public void sendEmail(Email msg) {
		
	System.out.println("Simulando envio de email...");
	System.out.println(msg.getSubject());
	System.out.println(msg.getFromAddress());
	System.out.println(msg.getToAddresses());
	System.out.println(msg.getSentDate());
	System.out.println("Email enviado");
		
	}

	@Override
	public void sendHtmlEmail(HtmlEmail sm) throws EmailException {
		System.out.println("Simulando envio de email...");
		System.out.println(sm.getSubject());
		System.out.println(sm.getFromAddress());
		System.out.println(sm.getToAddresses());
		System.out.println(sm.getSentDate());
		System.out.println("Email enviado");
	}


}
