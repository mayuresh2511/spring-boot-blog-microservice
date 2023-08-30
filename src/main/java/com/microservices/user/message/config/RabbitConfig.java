package com.microservices.user.message.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.amqp.core.Queue;


@Configuration
public class RabbitConfig {
	
	@Value("${queue.name}")
	private String queueName;
	
	@Bean
	public Queue queue() {
		return new Queue(queueName, true);
	}
//	@Bean
//	public DirectExchange deadLetterExchange() {
//		return new DirectExchange("deadLetterExchange");
//	}
//	
//	@Bean
//	public DirectExchange javainuseExchange() {
//		return new DirectExchange("javainuseExchange");
//	}
//	
//	@Bean
//	public Queue dlq() {
//		return QueueBuilder.durable("deadLetter.queue").build();
//	}
//	
//	@Bean
//	public Queue queue() {
//		return QueueBuilder.durable("javainuse.queue").withArgument("x-dead-letter-exchange", "deadLetterExchange")
//				.withArgument("x-dead-letter-routing-key", "deadLetter").build();
//	}
//	
//	@Bean
//	public Binding DLQbinding() {
//		return BindingBuilder.bind(dlq()).to(deadLetterExchange()).with("deadLetter");
//	}
//	
//	@Bean 
//	public Binding binding() {
//		return BindingBuilder.bind(queue()).to(javainuseExchange()).with("javainuse");
//	}
//	
//	@Bean
//	public MessageConverter jsonMessageConverter() {
//		return new Jackson2JsonMessageConverter();
//	}
//	
//	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//		rabbitTemplate.setMessageConverter(jsonMessageConverter());
//		return rabbitTemplate;
//	}
}
