package com.esl.SysDogN.security;

import org.apache.shiro.realm.jdbc.JdbcRealm;


/*
 * Classe para passar dados básicos ao shiro sobre querys usadas
 * na autenticação e autorização
 */
public class MyRealm extends JdbcRealm {

	
	HikariShiroDataSource ds = new HikariShiroDataSource();
	
	public MyRealm() {
		super();
		this.setAuthenticationQuery("SELECT senha from usuario where email = ?");
		this.setUserRolesQuery("SELECT tipo FROM usuario WHERE email = ?");
		this.setDataSource(ds);
		
	}

}
