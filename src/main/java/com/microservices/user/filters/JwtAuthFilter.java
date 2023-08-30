package com.microservices.user.filters;

import com.microservices.user.config.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private UserDetailsService userDetailsService;
  private JwtUtils jwtUtils;
  private Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

  public JwtAuthFilter(UserDetailsService userDetailsService, JwtUtils jwtUtils) {
    this.userDetailsService = userDetailsService;
    this.jwtUtils = jwtUtils;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    logger.info("Inside Custom filter implementation to handle JWT Token");

    final String authHeader = request.getHeader("Authorization");
    final String userName;
    final String jwtToken;

    if (authHeader == null || !authHeader.startsWith("Bearer")){
      logger.info("Bearer token not found in header");
      filterChain.doFilter(request, response);
      return;
    }

    logger.info("Bearer token found in header");

    jwtToken = authHeader.substring(7);
    userName = jwtUtils.extractUsername(jwtToken);

    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
      UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
      final boolean isValidToken = jwtUtils.validateToken(jwtToken, userDetails);

      if (isValidToken){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }

      filterChain.doFilter(request, response);
    }
  }
}
