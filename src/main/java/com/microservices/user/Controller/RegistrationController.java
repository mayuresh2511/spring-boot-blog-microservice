package com.microservices.user.Controller;

import com.microservices.user.Service.RegistrationService;
import com.microservices.user.model.UserRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class RegistrationController {

    final private Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    final private RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @PostMapping("register")
    public ResponseEntity<String> userRegistration(@RequestBody UserRegistrationRequest userData){

        System.out.println("Inside of registration controller");
        try{
            boolean isRegistered = registrationService.registerUser(userData);
            if (isRegistered){
                return ResponseEntity.ok().body("You are successfully registered");
            }
            return ResponseEntity.internalServerError().body("Facing technical issue right now");
        }catch(Exception e){
            System.out.println("Exception occured while registering user: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Facing technical issue right now");
        }
    }
}
