package com.microservices.user.model;

import java.time.LocalDateTime;

public abstract class BaseResponseImpl implements BaseResponse{

    private LocalDateTime timeStamp;
    private int statusCode;
    private String message;

    public BaseResponseImpl(int statusCode, String message){
        this.timeStamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    @Override
    public void setMessage(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    @Override
    public int getStatusCode() {
        return statusCode;
    }
}
