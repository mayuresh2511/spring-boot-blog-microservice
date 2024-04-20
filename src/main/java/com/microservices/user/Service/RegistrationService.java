package com.microservices.user.Service;

import com.microservices.user.Entity.UserEntity;
import com.microservices.user.Repository.UserRepository;
import com.microservices.user.dto.UserDetails;
import com.microservices.user.model.UserRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    final private Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private RabbitTemplate rabbitTemplate;
    final private Queue queue;
    public RegistrationService(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               RabbitTemplate rabbitTemplate,
                               Queue queue){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }
    public boolean registerUser(UserRegistrationRequest userData) {

        try{
            UserEntity user = new UserEntity();
            String encodedPassword = passwordEncoder.encode(userData.getPassword());
            copyAllFields(user, userData, encodedPassword);
            userRepository.save(user);

            UserDetails userDetails = new UserDetails(userData);
            rabbitTemplate.convertAndSend(this.queue.getName(), userDetails);
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
