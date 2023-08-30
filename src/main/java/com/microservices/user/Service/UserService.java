package com.microservices.user.Service;

import com.microservices.user.dto.AuthenticationRequest;
import com.microservices.user.dto.AuthenticationResponse;
import com.microservices.user.vo.Department;
import io.lettuce.core.output.ScoredValueScanOutput;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservices.user.Entity.User;
import com.microservices.user.Repository.UserRepository;
import com.microservices.user.vo.ResponsTemplateVo;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {


	private UserRepository userRepository;
	private RestTemplate restTemplate;

	public UserService(UserRepository userRepository, RestTemplate restTemplate) {
		this.userRepository = userRepository;
		this.restTemplate = restTemplate;
	}

	public User addUser(User user) {
		// TODO Auto-generated method stub
		System.out.println(user.getUserName());
		return userRepository.save(user);
	}

	public ResponsTemplateVo getAnyUserAndDepInfo(Long userId) {
		// TODO Auto-generated method stub
		ResponsTemplateVo vo = new ResponsTemplateVo();
		User user = userRepository.findById(userId).get();

		AuthenticationRequest authRequest = new AuthenticationRequest("user-service",
				"#autheforuserservice#");
		HttpEntity<AuthenticationRequest> requestEntity = new HttpEntity<>(authRequest);

		ResponseEntity<AuthenticationResponse> authResponse = restTemplate.postForEntity(
				"http://localhost:9091/api/v2/auth",
				requestEntity,
				AuthenticationResponse.class
		);

		System.out.println("Auth response code : " + authResponse.getStatusCode());
		System.out.println("Auth body : " + authResponse.getBody().toString());

		ResponseEntity<Department> response = restTemplate.getForEntity(
				"http://localhost:9091/deparments/" + user.getUserId(),
				Department.class);

		System.out.println(response.getStatusCode());
		System.out.println(response);
		vo.setDepartment(response.getBody());
		vo.setUser(user);
		return vo;
	}

	public ResponsTemplateVo getUserAndDepInfo(HttpSession session) {

		ResponsTemplateVo vo = new ResponsTemplateVo();
		Long userId = session.getAttribute("userId") != null ?
				(Long) session.getAttribute("userId") : null;
//		User user = userRepository.findById(userId).get();
		ResponseEntity<String> response = restTemplate.getForEntity("http://DEPARTMENT-SERVICE/deparments/1",
				String.class);
//		System.out.println(department);
//		vo.setDepartment(department);
//		vo.setUser(user);
		System.out.println(response.getStatusCode());
		System.out.println(response.getBody());
		return vo;

	}

	public void updateUserInfo(User user) {
		userRepository.save(user);
	}

	public void deleteUserInfo(HttpSession session) {
		Long userId = session.getAttribute("userId") != null ?
				(Long) session.getAttribute("userId") : null;
		userRepository.deleteById(userId);
	}
}
