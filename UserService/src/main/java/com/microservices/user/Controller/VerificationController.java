package com.microservices.user.Controller;

import com.microservices.user.Service.VerificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/api/v1/")
public class VerificationController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService){
        this.verificationService = verificationService;
    }

    @GetMapping("generateOtp/email")
    public ResponseEntity<String> generateOtp(HttpServletRequest request){
        verificationService.generateOtp(request.getHeader("loggedInUser"));
        return ResponseEntity.ok().body("OTP Generated Successfully");
    }

    @PostMapping("verifyOtp/email")
    public ResponseEntity<String> verifyOtp(@RequestParam("EMAIL_OTP") String emailOtp, HttpServletRequest request){
        verificationService.verifyOtp(request.getHeader("loggedInUser"), emailOtp);
        return ResponseEntity.ok().body("OTP Verified Successfully");
    }
}
