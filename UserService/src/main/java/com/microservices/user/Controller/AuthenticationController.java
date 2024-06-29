package com.microservices.user.Controller;

import com.microservices.user.Service.AuthenticationService;
import com.microservices.user.config.security.CustomUserDetails;
import com.microservices.user.model.UserRegistrationRequest;
import com.microservices.user.utils.JwtUtils;
import com.microservices.user.model.AuthenticationRequest;
import com.microservices.user.model.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/api/v1/")
public class AuthenticationController {

  final private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
  final private AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("auth")
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
    AuthenticationResponse authResponse;
    authResponse = authenticationService.authenticate(authenticationRequest);
    logger.info("Final Authentication Response " + authResponse);
    return ResponseEntity.ok(authResponse);
  }

  @PostMapping("register")
  public ResponseEntity<String> userRegistration(@RequestBody UserRegistrationRequest userData){
    logger.info("Inside of registration controller");
    boolean isRegistered = authenticationService.registerUser(userData);
    if (isRegistered){
      return ResponseEntity.ok().body("You are successfully registered");
    }
    return ResponseEntity.internalServerError().body("Facing technical issue right now");
  }
}
