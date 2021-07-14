package com.ssafy.pjt1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfig {

    //s3 access id와 pw를 호출한다.
	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;
	
	@Bean
	public BasicAWSCredentials AwsCredentials() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
		return awsCreds;
	}
	
	@Bean
	public AmazonS3 AwsS3Client() {
		
		AmazonS3 s3Builder = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.AP_NORTHEAST_2)
				.withCredentials(new AWSStaticCredentialsProvider(this.AwsCredentials()))
				.build();
		
		return s3Builder;
	}
}
