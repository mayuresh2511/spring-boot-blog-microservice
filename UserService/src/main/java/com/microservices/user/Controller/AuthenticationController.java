package com.microservices.user.Controller;

import com.microservices.user.Service.AuthenticationService;
import com.microservices.user.config.security.CustomUserDetails;
import com.microservices.user.model.UserRegistrationRequest;
import com.microservices.user.model.UserRegistrationResponse;
import com.microservices.user.utils.JwtUtils;
import com.microservices.user.model.AuthenticationRequest;
import com.microservices.user.model.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<UserRegistrationResponse> userRegistration(@RequestBody UserRegistrationRequest userData){
    logger.info("Inside of registration controller");
    UserRegistrationResponse userRegistrationResponse;
    userRegistrationResponse = authenticationService.registerUser(userData);
    return ResponseEntity.ok(userRegistrationResponse);
  }

  @GetMapping("refresh")
  public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request){
    AuthenticationResponse authResponse;
    authResponse = authenticationService.refreshToken(request.getHeader("loggedInUser"));
    return ResponseEntity.ok(authResponse);
  }
}
