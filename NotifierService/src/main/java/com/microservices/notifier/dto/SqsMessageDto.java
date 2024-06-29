package com.microservices.notifier.dto;

public record SqsMessageDto (String Type,
                             String MessageId,
                             String TopicArn,
                             String Subject,
                             String Message,
                             String Timestamp,
                             String SignatureVersion,
                             String Signature,
                             String SigningCertURL,
                             String UnsubscribeURL){
}
