package com.spring.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.project.entities.Contact;
import com.spring.project.entities.User;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	@Query("from Contact as c where c.user.id =:userId")
	// currentPage-page
	// Contact per page - 5
	public Page<Contact> findContactByUser(@Param("userId")int userId , Pageable pageable );

	// search
	public List<Contact> findByNameContainingAndUser(String name , User user);
}
