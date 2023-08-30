package com.microservices.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class CustomeUserDetailsService implements UserDetailsService {

  private Logger logger = LoggerFactory.getLogger(CustomeUserDetailsService.class);

  private final static List<UserDetails> APPLICATION_USERS = Arrays.asList(
      new User(
          "maymar25",
          "25nov1998",
          Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
      ),
      new User(
          "mihmar",
          "30oct1993",
          Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
      )
  );

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("Inside CustomUserDetailsService loadUserByUsername method");

    return APPLICATION_USERS
        .stream()
        .filter(user -> user.getUsername().equalsIgnoreCase(username))
        .findFirst()
        .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
  }
}
