package com.spring.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.spring.project.entities.Contact;
import com.spring.project.entities.User;
import com.spring.project.repository.ContactRepository;
import com.spring.project.repository.UserRepository;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	// Search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query, Principal principal){
		
		User user = this.userRepository.findByEmail(principal.getName());
		List<Contact> contact = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contact);
	}
}
