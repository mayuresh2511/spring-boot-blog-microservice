package com.microservices.user.model;

public class UserRegistrationResponse extends BaseResponseImpl{
    private String userName;
    public UserRegistrationResponse(String userName, int statusCode, String message) {
        super(statusCode, message);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
