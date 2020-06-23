package com.esl.SysDogN.teste.S3;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class PropertiesTest {
	
	public static Properties getProp() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(
                "./src/main/resources/application.properties");
        props.load(file);
        return props;
 
    }
	
	@Test
	public void imprimir() throws IOException{
		Properties prop = getProp();
		String remetente = prop.getProperty("default.sender");
		String destinatario = prop.getProperty("default.recipient");
		
		System.out.println("remetente: " + remetente);
		System.out.println("destinatario: " + destinatario);
	}

}
