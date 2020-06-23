package com.esl.SysDogN.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.primefaces.event.FileUploadEvent;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.esl.SysDogN.config.S3config;

public class S3Service implements Serializable{
	private static final long serialVersionUID = 1L;
	
	 S3config s3 = new S3config();
	 
	 private String bucketName = "sysdog";
	
	
public void uploadFile(FileUploadEvent event) throws Exception {
	
	try {
		String fileName = event.getFile().getFileName();//extrai o nome do arquivo que foi enviado
		InputStream is = event.getFile().getInputstream();
		String contentType = event.getFile().getContentType();//extrai o tipo do aruivo enviado(.jpg,.txt)
		 uploadFile(is, fileName, contentType);
			
		} catch (IOException e) {
			throw new Exception("Erro de IO: " +e.getMessage());
		}
}


public void uploadFile(InputStream is,String fileName,String contentType) throws Exception {
    
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentType(contentType);
		s3.s3client().putObject(bucketName, fileName, is, meta);

	}

public void deleteFile(String fileName) {
	
	s3.s3client().deleteObject(bucketName, fileName);
}

/*
 * Classe criada com base no que foi passado pelo profesor Nelio alves usando métodos sobrecarredos para 
 * inserção de dados
 * 
 * o Campo buckName refere-se ao nome do seu bucket
 */



}
