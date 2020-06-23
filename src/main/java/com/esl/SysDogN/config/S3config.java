package com.esl.SysDogN.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

//Configuração para upload de imagens no Amazon S3 
public class S3config {

	
	
	private String awsId = "";
	private String awsKey = "";
	private String region = "";
	
	
	public AmazonS3 s3client() {
		BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
							.withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
        return s3client;		
	}
	

	
	/*
	 * OS campos acima(awsId, awsKey, region) devem ser preenchidos com os dados fornecidos pela amazon 
	 */
	
}
