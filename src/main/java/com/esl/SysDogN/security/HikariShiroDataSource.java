package com.esl.SysDogN.security;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * Usando hikari para providenciar o datasource para o shiro
 */

public class HikariShiroDataSource extends HikariDataSource {
	
	public HikariShiroDataSource() {
		super();
		this.setDriverClassName("org.mariadb.jdbc.MariaDbDataSource");
		this.setJdbcUrl("jdbc:mariadb://localhost:3306/sysdog3");
		this.setUsername("root");
		this.setPassword("base1805");
	}

	public HikariShiroDataSource( HikariConfig config) {
		super(config);
		
	}
}
