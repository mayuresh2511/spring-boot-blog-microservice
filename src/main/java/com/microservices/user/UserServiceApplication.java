package com.microservices.user;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.Gson;
import com.microservices.user.dto.UserDetails;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableRabbit
@SpringBootApplication(scanBasePackages="com.microservices")
public class UserServiceApplication implements CommandLineRunner {
	@Autowired
	private AmazonSNS amazonSNSClient;

	@Value("${user.service.testEnvVariable}")
	private String testEnvVariable;

	@Value("${aws.snsArn}")
	private String snsArn;

	private Gson gson = new Gson();
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Testing the SNS connection");
		UserDetails userDetails = new UserDetails("Mihir",
				"Marathe",
				"mihmar@gail.com",
				"8976512345");

		PublishResult publishResult = amazonSNSClient.publish(new PublishRequest
				(snsArn, gson.toJson(userDetails))
		);

		System.out.println("Publish result => " + publishResult.getMessageId());
		System.out.println("Testing env variable => " + testEnvVariable);

		System.out.println("Test variable : " + System.getenv("JAVA_HOME"));
		System.out.println("Test variable : " + System.getenv("TEST_VARIABLE"));
	}

}
