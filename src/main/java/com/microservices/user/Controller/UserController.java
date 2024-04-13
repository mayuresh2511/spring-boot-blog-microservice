package com.microservices.user.Controller;

import com.microservices.user.Entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.user.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/")
public class UserController {

	@Autowired
	private UserService userService;

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("guest")
	public String guestUser(HttpServletRequest request){

		return "Hello Guest User";
	}
	
	@PostMapping("admin/addUser")
	public UserEntity addUser(@RequestBody UserEntity user) {
		logger.info("User is : " + user);
		return userService.addUser(user);
	}

	@PutMapping("user/updateSelfInfo")
	public String updateUserInfo(@RequestBody UserEntity user){
		userService.updateUserInfo(user);
		return "User Info Updated Successfully";
	}

	@DeleteMapping("user/deleteMyInfo")
	public String deleteUserInfo(HttpSession session){
		userService.deleteUserInfo(session);
		return "User Info Deleted Successfully";
	}
	
}
