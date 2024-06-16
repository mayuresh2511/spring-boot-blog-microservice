package com.microservices.user.Controller;

import com.microservices.user.Service.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api/v1/")
public class VerificationController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService){
        this.verificationService = verificationService;
    }

    @GetMapping("generateOtp")
    public ResponseEntity<String> generateOtp(){
        verificationService.generateOtp();
        return ResponseEntity.ok().body("OTP Generated Successfully");
    }
}
