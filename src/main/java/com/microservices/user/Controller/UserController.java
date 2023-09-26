package com.microservices.user.Controller;

import com.microservices.user.filters.JwtAuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.user.Entity.User;
import com.microservices.user.Service.UserService;
import com.microservices.user.vo.ResponsTemplateVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("guest")
	public String guestUser(HttpServletRequest request, HttpSession session){

		System.out.println("Session Id is " + session.getId());

		return "Hello Guest User";
	}
	
	@PostMapping("user/addUser")
	public User addUser(@RequestBody User user) {
		logger.info("User is : " + user);
		return userService.addUser(user);
	}

	@GetMapping("user/getUserDetails")
	public ResponsTemplateVo getUserAndDepInfo(HttpSession session) {
		return userService.getUserAndDepInfo(session);
	}

	@GetMapping("admin/{userId}")
	public ResponsTemplateVo getAnyUserAndDepInfo(@PathVariable Long userId) {
		return userService.getAnyUserAndDepInfo(userId);
	}

	@PutMapping("user/updateSelfInfo")
	public String updateUserInfo(@RequestBody User user){
		userService.updateUserInfo(user);
		return "User Info Updated Successfully";
	}

	@DeleteMapping("user/deleteMyInfo")
	public String deleteUserInfo(HttpSession session){
		userService.deleteUserInfo(session);
		return "User Info Deleted Successfully";
	}
	
}
