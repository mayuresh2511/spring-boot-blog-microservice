package com.microservices.user.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.user.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
