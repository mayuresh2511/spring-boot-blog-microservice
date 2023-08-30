package com.microservices.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private UserDetailsService userDetailsService;
  private Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

  public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    logger.info("Inside Custom AuthenticationProvider");

    String userName = authentication.getName();
    String password = authentication.getCredentials().toString();

    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

    if (userDetails != null){
      if (password.equals(userDetails.getPassword())){
        return new UsernamePasswordAuthenticationToken(userName, password, userDetails.getAuthorities());
      }else {
        throw new BadCredentialsException("Incorrect username or password");
      }
    }else {
      throw new UsernameNotFoundException("User does not exist");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
