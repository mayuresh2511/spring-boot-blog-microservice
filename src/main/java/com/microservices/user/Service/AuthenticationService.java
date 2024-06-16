package com.microservices.user.Service;

import com.microservices.user.Entity.UserEntity;
import com.microservices.user.Repository.UserRepository;
import com.microservices.user.config.security.CustomUserDetails;
import com.microservices.user.model.AuthenticationRequest;
import com.microservices.user.model.AuthenticationResponse;
import com.microservices.user.model.UserRegistrationRequest;
import com.microservices.user.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    final private RabbitTemplate rabbitTemplate;
    final private Queue queue;
    public AuthenticationService(UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 UserDetailsService userDetailsService,
                                 JwtUtils jwtUtils,
                                 PasswordEncoder passwordEncoder,
                                 RabbitTemplate rabbitTemplate,
                                 Queue queue){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }
    public boolean registerUser(UserRegistrationRequest userData) {

        UserEntity user = new UserEntity();
        String encodedPassword = passwordEncoder.encode(userData.getPassword());
        copyAllFields(user, userData, encodedPassword);
        userRepository.save(user);

        //TODO: Uncomment later
        // UserDetails userDetails = new UserDetails(userData);
        // rabbitTemplate.convertAndSend(this.queue.getName(), userDetails);

        return true;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        logger.info("Inside authentication service class");

        AuthenticationResponse authResponse = new AuthenticationResponse();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword())
        );

        CustomUserDetails user = (CustomUserDetails)userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        Map<String, Object> claimsHashMap = new HashMap<>();
        claimsHashMap.put("USER_ROLE", user.getUserRole());
        claimsHashMap.put("SUBSCRIPTION_CATEGORY", user.getSubscriptionCategory());
        claimsHashMap.put("IS_EMAIL_VERIFIED", user.isEmailVerified());

        String jwtToken = jwtUtils.generateToken(claimsHashMap, authenticationRequest.getUsername());

        authResponse.setUsername(authenticationRequest.getUsername());
        authResponse.setAuthSuccessful(true);
        authResponse.setJwtToken(jwtToken);
        authResponse.setAuthMessage("User Verified Successfully");

        return authResponse;
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
}
