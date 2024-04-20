package com.microservices.user.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  final private Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
  final private UserDetailsService userDetailsService;
  final PasswordEncoder passwordEncoder;
  public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    logger.info("Inside Custom AuthenticationProvider");

    String userName = authentication.getName();
    String password = authentication.getCredentials().toString();

    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

    logger.info("User details password : " + userDetails.getPassword() + " encoded pass : " + password);

    if (!passwordEncoder.matches(password, userDetails.getPassword())){
      throw new BadCredentialsException("Incorrect username or password");
    }
    return new UsernamePasswordAuthenticationToken(userName, password, userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
