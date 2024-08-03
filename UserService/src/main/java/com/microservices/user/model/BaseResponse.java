package com.microservices.user.model;

import java.time.LocalDateTime;

public interface BaseResponse {

    LocalDateTime getTimeStamp();
    void setMessage(String message);
    String getMessage();
    void setStatusCode(int statusCode);
    int getStatusCode();
}
