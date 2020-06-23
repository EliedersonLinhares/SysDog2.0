package com.esl.SysDogN.security;

import org.apache.shiro.authc.credential.PasswordService;
import org.mindrot.jbcrypt.BCrypt;



/*
 * Classe que implementa um novo PasswordService do apache shiro para que possa usar o bcrypt
 * para encriptação
 */
public class BCryptPasswordService implements PasswordService {

	
	   public static final int DEFAULT_BCRYPT_ROUND = 10;
	    private int logRounds;

	    public BCryptPasswordService() {
	        this.logRounds = DEFAULT_BCRYPT_ROUND;
	    }

	    public BCryptPasswordService(int logRounds) {
	        this.logRounds = logRounds;
	    }

	    //Encripitar a senha
	    @Override
	    public String encryptPassword(Object plaintextPassword) {
	        if (plaintextPassword instanceof String) {
	            String password = (String) plaintextPassword;
	            return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
	        }
	        throw new IllegalArgumentException(
	            "BCryptPasswordService encryptPassword suporta somente credenciais do tipo java.lang.String.");
	    }

	    //Verificar se a senha esta correta
	    @Override
	    public boolean passwordsMatch(Object submittedPlaintext, String encrypted) {
	        if (submittedPlaintext instanceof char[]) {
	            String password = String.valueOf((char[]) submittedPlaintext);
	            return BCrypt.checkpw(password, encrypted);
	        }
	        throw new IllegalArgumentException(
	            "BCryptPasswordService passwordsMatch suporta somente credenciais do tipo char[].");
	    }

	    public void setLogRounds(int logRounds) {
	        this.logRounds = logRounds;
	    }

	    public int getLogRounds() {
	        return logRounds;
	    }
}
