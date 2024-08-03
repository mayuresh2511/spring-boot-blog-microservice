package com.microservices.user.Service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.Gson;
import com.microservices.user.Entity.UserEntity;
import com.microservices.user.Repository.UserRepository;
import com.microservices.user.config.security.CustomUserDetails;
import com.microservices.user.dto.awsSns.UserDetails;
import com.microservices.user.model.AuthenticationRequest;
import com.microservices.user.model.AuthenticationResponse;
import com.microservices.user.model.UserRegistrationRequest;
import com.microservices.user.model.UserRegistrationResponse;
import com.microservices.user.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    final private Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    final private UserRepository userRepository;
    final private AuthenticationManager authenticationManager;
    final private UserDetailsService userDetailsService;
    final private JwtUtils jwtUtils;
    final private PasswordEncoder passwordEncoder;
    final private AmazonSNS amazonSNSClient;
    @Value("${aws.snsArn}")
    private String snsArn;

    public AuthenticationService(UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 UserDetailsService userDetailsService,
                                 JwtUtils jwtUtils,
                                 PasswordEncoder passwordEncoder,
                                 AmazonSNS amazonSNSClient){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.amazonSNSClient = amazonSNSClient;
    }
    public UserRegistrationResponse registerUser(UserRegistrationRequest userData) {

        UserEntity user = new UserEntity();
        String encodedPassword = passwordEncoder.encode(userData.getPassword());
        copyAllFields(user, userData, encodedPassword);
        userRepository.save(user);

        UserDetails userDetails = new UserDetails(userData);
        Gson gson = new Gson();
        PublishResult publishResult = amazonSNSClient.publish(new PublishRequest
                (snsArn, gson.toJson(userDetails), "User Registration Event")
        );

        System.out.println("Publish result => " + publishResult.getMessageId());

        return new UserRegistrationResponse(
                userData.getUsername(),
                100,
                "User Registered Successfully");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        logger.info("Inside authentication service class");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword())
        );

        CustomUserDetails user = (CustomUserDetails)userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        Map<String, Object> claimsHashMap = new HashMap<>();
        claimsHashMap.put("USER_ROLE", user.getUserRole());
        claimsHashMap.put("SUBSCRIPTION_CATEGORY", user.getSubscriptionCategory());
        claimsHashMap.put("IS_EMAIL_VERIFIED", user.isEmailVerified());

        String jwtAccessToken = jwtUtils.generateAccessToken(claimsHashMap, authenticationRequest.getUsername());
        String jwtRefreshToken = jwtUtils.generateRefreshToken(authenticationRequest.getUsername());

        return new AuthenticationResponse(
                authenticationRequest.getUsername(),
                true,
                jwtAccessToken,
                jwtRefreshToken,
                100,
                "User Verified Successfully");
    }
    private void copyAllFields(UserEntity user, UserRegistrationRequest userData, String encodedPassword){
        user.setUserName(userData.getUsername());
        user.setUserPassword(encodedPassword);
        user.setUserFirstName(userData.getFirstName());
        user.setUserLastName(userData.getLastName());
        user.setUserEmailId(userData.getEmailAddress());
        user.setUserMobileNumber(userData.getMobileNumber());
        user.setUserRole("ROLE_USER");
        user.setSubscriptionCategory("DEFAULT");
        user.setEmailVerified(false);
        user.setEmailVerificationOtp(null);
        user.setMobileVerified(false);
        user.setMobileVerificationOtp(null);
    }

    public AuthenticationResponse refreshToken(String loggedInUser) {
        UserEntity userEntity = userRepository.findByUserName(loggedInUser);

        Map<String, Object> claimsHashMap = new HashMap<>();
        claimsHashMap.put("USER_ROLE", userEntity.getUserRole());
        claimsHashMap.put("SUBSCRIPTION_CATEGORY", userEntity.getSubscriptionCategory());
        claimsHashMap.put("IS_EMAIL_VERIFIED", userEntity.isEmailVerified());

        String jwtAccessToken = jwtUtils.generateAccessToken(claimsHashMap, loggedInUser);
        String jwtRefreshToken = jwtUtils.generateRefreshToken(loggedInUser);

        return new AuthenticationResponse(
                loggedInUser,
                true,
                jwtAccessToken,
                jwtRefreshToken,
                100,
                "Refreshed Successfully");
    }
}
