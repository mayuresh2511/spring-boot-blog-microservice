package com.microservices.user.Service;

import com.microservices.user.exception.custom.InvalidOtpProvidedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationService {
    final private Logger logger = LoggerFactory.getLogger(VerificationService.class);
    public void generateOtp() {
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);
        logger.info("OTP Generated : " + otp);
        //TODO: Implement logic to push OTP object in RabbitMQ or Kafka and update in DB
    }

    public void validateOtp(){

        boolean isValidOtp = false;

        if (!isValidOtp){
            throw new InvalidOtpProvidedException("Incorrect OTP Provided");
        }

    }
}
