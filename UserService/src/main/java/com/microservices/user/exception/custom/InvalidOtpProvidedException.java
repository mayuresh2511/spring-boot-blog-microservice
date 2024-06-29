package com.microservices.user.exception.custom;

public class InvalidOtpProvidedException extends RuntimeException{
    public InvalidOtpProvidedException(String message){
        super(message);
    }
}
