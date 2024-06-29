package com.microservices.user.Repository;

import com.microservices.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

    UserEntity findByUserName(String userName);

    UserEntity deleteByUserName(String userName);

    @Modifying
    @Query("update UserEntity set emailVerificationOtp = :otp where userName = :name")
    int updateUserSetEmailVerificationOtpForUserName(@Param("otp") String otp, @Param("name") String name);

    @Query("select emailVerificationOtp from UserEntity where userName = :name")
    String findEmailVerificationOtpByUserName(@Param("name") String name);

    @Modifying
    @Query("update UserEntity set isEmailVerified = :isVerified where userName = :name")
    int updateUserSetIsEmailVerifiedForUserName(@Param("isVerified") boolean isVerified, @Param("name") String name);
}
