package com.microservices.user.config;

import com.microservices.user.filters.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private JwtAuthFilter jwtAuthFilter;
  private UserDetailsService userDetailsService;
  private AuthenticationProvider authenticationProvider;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                        UserDetailsService userDetailsService,
                        AuthenticationProvider authenticationProvider) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.userDetailsService = userDetailsService;
    this.authenticationProvider = authenticationProvider;
  }

  /** Security chain for Username Password authentication and authorization **/
//  @Bean
//  public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
//
//    http
//        .csrf().disable()
//        .authorizeRequests()
//        .antMatchers("/**/auth/**")
//        .permitAll()
//        .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
//        .anyRequest()
//        .authenticated()
//        .and()
//        .sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//        .and()
//        .authenticationProvider(authenticationProvider)
//        .formLogin();
//
//    return http.build();
//  }

  /** Security filter chain for JWT authorization **/
  @Bean
  public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {

    http
        .csrf(csrf -> csrf.disable())
        .authorizeRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/**/auth/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAuthority("ROLE_ADMIN")
                .anyRequest()
                .authenticated()
        )
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

//  @Bean
//  public AuthenticationProvider authenticationProvider(){
//    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//    authenticationProvider.setUserDetailsService(userDetailsService);
//    authenticationProvider.setPasswordEncoder(passwordEncoder());
//    return authenticationProvider;
//  }

//  @Bean
//  public PasswordEncoder passwordEncoder(){
//    return NoOpPasswordEncoder.getInstance();
//  }
}
