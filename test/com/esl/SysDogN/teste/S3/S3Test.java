package com.esl.SysDogN.teste.S3;

import java.io.File;

import org.junit.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.esl.SysDogN.config.S3config;



public class S3Test {
	

	
	
	
	 S3config s3 = new S3config();
	 
	 private String bucketName = "sysdog";
	
	
	
	
	
public void uploadFile(String localFilePath) {
	
	try {
	File file = new File(localFilePath);
	 System.out.println("iniciando o upload");
	s3.s3client().putObject(new PutObjectRequest(bucketName, "teste2.jpg", file));
	 System.out.println("Upload finalizado");
	}
	catch(AmazonServiceException e) {
		e.getErrorMessage();
		e.getErrorCode();
	}
	catch(AmazonClientException e) {
		 e.getMessage();
	}
}

@Test
public void upload() {
	uploadFile("C:\\Spring\\temp\\s1.jpg");
}


}