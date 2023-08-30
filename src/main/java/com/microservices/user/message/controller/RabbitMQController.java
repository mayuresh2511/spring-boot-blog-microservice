package com.microservices.user.message.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.user.message.model.Employee;

@RestController
@RequestMapping(value = "/javainuse-rabbitmq")
public class RabbitMQController {

	@Autowired
	private AmqpTemplate amqpTemplate;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private Queue queue;
	
	@GetMapping(value = "/producer")
	public String producer() {
		rabbitTemplate.convertAndSend(this.queue.getName(), "Hello from RabbitMQ Publisher");
		return "Message sent to RabbitMQ successfully";
	}
//	public String producer() {
//		Employee emp = new Employee();
//		emp.setEmpName("Mayuresh");
//		emp.setEmpId("1234");
//		emp.setSalary(1000);
//		amqpTemplate.convertAndSend("javainuseExchange", "javainuse", emp);
//		return "Message sent to RabbitMQ successfully";
//	}
	
}
