package com.microservices.notifier.config.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class AwsConfiguration {
    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;
//    @Value("${aws.sqsUri}")
//    private String sqsUri;
    @Value("${aws.region}")
    private String region;
    private AwsCredentials getCredentialsSqs(){
        return AwsBasicCredentials.create(
                accessKey,
                secretKey
        );
    }

    private AWSCredentials getCredentials(){
        return new BasicAWSCredentials(
                accessKey,
                secretKey
        );
    }

    @Bean
    public SqsAsyncClient amazonSQS(){
        try {
            return SqsAsyncClient.builder()
                    .endpointOverride(new URI("https://sqs.ap-south-1.amazonaws.com/343167039326/java-microservice-user-queue"))
                    .credentialsProvider(StaticCredentialsProvider.create(getCredentialsSqs()))
                    .region(Region.AP_SOUTH_1)
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public AmazonSNS amazonSNSClient(){
        return AmazonSNSClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                .withRegion(Regions.valueOf(region))
                .build();

    }
}
