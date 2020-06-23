package com.esl.SysDogN.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.omnifaces.util.Messages;

//Classe para editar as mensagens na tela
public class FacesUtil {

	public static void addInfoMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
		
	}
	
	 public static void addDetailMessage(String message) {
	        addDetailMessage(message, null);
	    }

	    public static void addDetailMessage(String message, FacesMessage.Severity severity) {
	        FacesMessage facesMessage = Messages.create("").detail(message).get();
	        if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
	            facesMessage.setSeverity(severity);
	        }
	        Messages.add(null, facesMessage);
	    }
	

}
