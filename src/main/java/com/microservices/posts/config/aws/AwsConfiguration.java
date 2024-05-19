package com.microservices.posts.config.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfiguration {
    private AWSCredentials getCredentials(){
        return new BasicAWSCredentials(
                "", // Removed the secrets by git rebase
                "" // Removed the secrets by git rebase
        );
    }
    @Bean
    public AmazonS3 amazonS3Client(){
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }
}
