package com.microservices.user;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableRabbit
@SpringBootApplication(scanBasePackages="com.microservices")
public class UserServiceApplication {

	private static ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
