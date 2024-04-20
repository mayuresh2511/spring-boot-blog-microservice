package com.microservices.user.config.security;

import com.microservices.user.Entity.UserEntity;
import com.microservices.user.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomeUserDetailsService implements UserDetailsService {

  final private Logger logger = LoggerFactory.getLogger(CustomeUserDetailsService.class);
  final private UserRepository userRepository;

  public CustomeUserDetailsService(UserRepository userRepository){
      this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.info("Inside CustomUserDetailsService loadUserByUsername method");

    UserEntity user = userRepository.findByUserName(username);

    if (user != null){
      return User.withUsername(user.getUserName())
              .password(user.getUserPassword())
              .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))
              .build();
    }else {
      throw new UsernameNotFoundException("User not registered with us !! Please try with registered username or register first");
    }
  }
}
