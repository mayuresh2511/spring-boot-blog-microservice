package com.microservices.notifier.controller;

import com.google.gson.Gson;
import com.microservices.notifier.dto.SqsMessageDto;
import com.microservices.notifier.service.NotificationService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Controller;
import software.amazon.awssdk.services.sqs.model.Message;

@Controller
public class NotificationController {

    final private NotificationService notificationService;

    public NotificationController (NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @SqsListener("java-microservice-user-queue" )
    public void listen(Message message) {

        System.out.println("Messages from queue => " + message.body());

        Gson gson = new Gson();

        SqsMessageDto sqsMessageDto = gson.fromJson(message.body(), SqsMessageDto.class);

        System.out.println(sqsMessageDto.toString());
    }

}
