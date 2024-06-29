package com.microservices.user.model;

import com.microservices.user.Entity.UserEntity;

public record UserResponseModel (String userName,
                                 String userFirstName,
                                 String userLastName,
                                 String userEmailId,
                                 String userMobileNumber,
                                 boolean isEmailVerified,
                                 boolean isMobileVerified,
                                 String subscriptionType){

    public UserResponseModel(UserEntity userEntity){
        this(userEntity.getUserName(),
                userEntity.getUserFirstName(),
                userEntity.getUserLastName(),
                userEntity.getUserEmailId(),
                userEntity.getUserMobileNumber(),
                userEntity.isEmailVerified(),
                userEntity.isMobileVerified(),
                userEntity.getSubscriptionCategory()
        );
    }
}
