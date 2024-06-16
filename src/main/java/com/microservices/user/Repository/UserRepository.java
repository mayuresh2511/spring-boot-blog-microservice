package com.microservices.user.Repository;

import com.microservices.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

    UserEntity findByUserName(String userName);

    UserEntity deleteByUserName(String userName);
}
