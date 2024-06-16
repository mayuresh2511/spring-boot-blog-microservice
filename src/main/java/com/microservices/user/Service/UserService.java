package com.microservices.user.Service;

import com.microservices.user.Entity.UserEntity;
import org.springframework.stereotype.Service;

import com.microservices.user.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {
	final private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserEntity addUser(UserEntity user) {
		System.out.println(user.getUserName());
		return userRepository.save(user);
	}

	public void updateUserInfo(UserEntity user) {
		userRepository.save(user);
	}

	public void deleteUserInfo(String userName) {
		userRepository.deleteByUserName(userName);
	}

}
