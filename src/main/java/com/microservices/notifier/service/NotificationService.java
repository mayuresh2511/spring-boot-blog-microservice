package com.microservices.notifier.service;

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    final private AmazonSNS amazonSNSClient;
    @Value("${aws.snsArn}")
    private String snsArn;

    public NotificationService(AmazonSNS amazonSNSClient){
        this.amazonSNSClient = amazonSNSClient;
    }

    public void sendMailToUser(){
        //TODO:To implement logic for sending mail to user.
    }

    public void sendMailToUsers(){
        //TODO: To implement logic for sending mail to multiple users.
    }

    public void sendMessageToUser(){
        //TODO: To implement the logic for sending SMS.
    }

    public void sendMessageToUsers(){
        //TODO: To implement the logic for sending SMS to multiple users.
    }
}
