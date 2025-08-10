package org._p1m.foodorderingsystem.config.beans;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
	@Value("${aws.region}")
    private String awsRegion;
    @Bean
    public AmazonS3 s3Client() {
        AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(awsRegion))
                .withCredentials(credentialsProvider)
                .build();
    }
}
