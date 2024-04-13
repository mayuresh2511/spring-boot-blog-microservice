package com.microservices.user.Service;

import com.microservices.user.Controller.RegistrationController;
import com.microservices.user.Entity.UserEntity;
import com.microservices.user.Repository.UserRepository;
import com.microservices.user.dto.UserRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    final private Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    public boolean registerUser(UserRegistrationRequest userData) {

        try{
            UserEntity user = new UserEntity();
            String encodedPassword = passwordEncoder.encode(userData.getPassword());
            copyAllFields(user, userData, encodedPassword);
            userRepository.save(user);
        }catch (Exception exception){
            logger.error("Exception while saving record in DB : " + exception.getMessage());
            return false;
        }

        return true;
    }

    private void copyAllFields(UserEntity user, UserRegistrationRequest userData, String encodedPassword){
        user.setUserName(userData.getUsername());
        user.setUserPassword(encodedPassword);
        user.setUserFirstName(userData.getFirstName());
        user.setUserLastName(userData.getLastName());
        user.setUserEmailId(userData.getEmailAddress());
        user.setUserMobileNumber(userData.getMobileNumber());
    }
}
