package com.esl.SysDogN.security;

import java.util.List;
import java.util.regex.Pattern;

/*
 * by Ankur
 * 
 * https://stackoverflow.com/questions/36097097/password-validate-8-digits-contains-upper-lowercase-and-a-special-character
 */

public class PasswordValidate {
	
	public static boolean isValid(String passwordhere,  List<String> errorList) {

	    Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
	    Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
	    Pattern lowerCasePatten = Pattern.compile("[a-z ]");
	    Pattern digitCasePatten = Pattern.compile("[0-9 ]");
	    errorList.clear();

	    boolean flag=true;

	   
	    if (passwordhere.length() < 8) {
	        errorList.add("A senha tem que ter ao menos 8 caracteres!");
	        flag=false;
	    }
	    if (!specailCharPatten.matcher(passwordhere).find()) {
	        errorList.add("A senha tem que ter ao menos um caracter special(ex. #,$,%)!");
	        flag=false;
	    }
	    if (!UpperCasePatten.matcher(passwordhere).find()) {
	        errorList.add("A senha tem que ter ao menos um caracter com letra maiuscula!");
	        flag=false;
	    }
	    if (!lowerCasePatten.matcher(passwordhere).find()) {
	        errorList.add("A senha tem que ter ao menos um caracter com letra minuscula!");
	        flag=false;
	    }
	    if (!digitCasePatten.matcher(passwordhere).find()) {
	        errorList.add("A senha tem que ter ao menos numero (0-9)!");
	        flag=false;
	    }

	    return flag;

	}

}
