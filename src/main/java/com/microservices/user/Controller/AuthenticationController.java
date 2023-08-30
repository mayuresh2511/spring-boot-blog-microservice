package com.microservices.user.Controller;

import com.microservices.user.config.JwtUtils;
import com.microservices.user.dto.AuthenticationRequest;
import com.microservices.user.dto.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthenticationController {

  private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
  private AuthenticationManager authenticationManager;
  private UserDetailsService userDetailsService;
  private JwtUtils jwtUtils;

  public AuthenticationController(AuthenticationManager authenticationManager,
                                  UserDetailsService userDetailsService,
                                  JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){

    logger.info("Inside authentication controller class");

    AuthenticationResponse authResponse = new AuthenticationResponse();

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
            authenticationRequest.getPassword())
    );

    UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    String jwtToken = jwtUtils.generateToken(user);


    if (user != null){
      authResponse.setUsername(authenticationRequest.getUsername());
      authResponse.setAuthSuccessful(true);
      authResponse.setJwtToken(jwtToken);
      return ResponseEntity.ok(authResponse);
    }

    authResponse.setUsername(authenticationRequest.getUsername());
    authResponse.setAuthSuccessful(false);
    authResponse.setJwtToken(null);

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(authResponse);
  }

}
