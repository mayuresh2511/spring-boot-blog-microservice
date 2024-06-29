package com.microservices.user.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.Gson;
import com.microservices.user.Repository.UserRepository;
import com.microservices.user.dto.awsSns.AwsSnsOtpEventDto;
import com.microservices.user.exception.custom.InvalidOtpProvidedException;
import com.microservices.user.model.UserResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationService {
    final private Logger logger = LoggerFactory.getLogger(VerificationService.class);
    final private UserService userService;
    final private AmazonSNS amazonSNSClient;
    final private UserRepository userRepository;
    @Value("${aws.snsArn}")
    private String snsArn;
    public VerificationService(UserService userService,
                               AmazonSNS amazonSNSClient,
                               UserRepository userRepository){
        this.userService = userService;
        this.amazonSNSClient = amazonSNSClient;
        this.userRepository = userRepository;
    }

    public void generateOtp(String userName) {
        Random random = new Random();
        int otpValue = 100_000 + random.nextInt(900_000);
        String otp = String.valueOf(otpValue);
        logger.info("OTP Generated : " + otp);

        UserResponseModel userResponseModel = userService.getUser(userName);
        AwsSnsOtpEventDto otpEventDto = new AwsSnsOtpEventDto(userName, userResponseModel.userEmailId(), otp);

        Gson gson = new Gson();
        PublishResult publishResult = amazonSNSClient.publish(new PublishRequest
                (snsArn, gson.toJson(otpEventDto))
        );

        System.out.println("Publish result => " + publishResult.getMessageId());

        int updateCount = userRepository.updateUserSetEmailVerificationOtpForUserName(otp, userName);

        System.out.println("User update count => " + updateCount);
    }

    public void verifyOtp(String userName, String emailOtp){

        String userOtp = userRepository.findEmailVerificationOtpByUserName(userName);

        if (!emailOtp.equalsIgnoreCase(userOtp)){
            throw new InvalidOtpProvidedException("Incorrect OTP Provided");
        }

        int updateCount = userRepository.updateUserSetIsEmailVerifiedForUserName(true, userName);

        System.out.println("User update count => " + updateCount);
    }
}
