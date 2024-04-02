package com.spring.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.project.entities.Contact;
import com.spring.project.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	
	public User findByEmail(@Param("email") String email);
	

}
