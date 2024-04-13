package com.microservices.user.Controller;

import com.microservices.user.config.JwtUtils;
import com.microservices.user.dto.AuthenticationRequest;
import com.microservices.user.dto.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  final private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
  final private AuthenticationManager authenticationManager;
  final private UserDetailsService userDetailsService;
  final private JwtUtils jwtUtils;

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

    try{
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                      authenticationRequest.getPassword())
      );

      UserDetails user = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
      String jwtToken = jwtUtils.generateToken(user);

      authResponse.setUsername(authenticationRequest.getUsername());
      authResponse.setAuthSuccessful(true);
      authResponse.setJwtToken(jwtToken);
      authResponse.setAuthMessage("User Verified Successfully");

    }catch (BadCredentialsException exception){
      logger.error("Exception while authentication: " + exception.getMessage());
      authResponse.setUsername(authenticationRequest.getUsername());
      authResponse.setAuthSuccessful(false);
      authResponse.setJwtToken(null);
      authResponse.setAuthMessage("Please enter correct username/password");
      return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }catch (UsernameNotFoundException exception){
      logger.error("Exception while authentication: " + exception.getMessage());
      authResponse.setUsername(authenticationRequest.getUsername());
      authResponse.setAuthSuccessful(false);
      authResponse.setJwtToken(null);
      authResponse.setAuthMessage("User not registered with us !! Please try with registered username or register first");
      return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    logger.info("Final Authentication Response " + authResponse);
    return ResponseEntity.ok(authResponse);
  }

}
