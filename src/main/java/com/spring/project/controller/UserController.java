package com.spring.project.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.project.entities.Contact;
import com.spring.project.entities.User;
import com.spring.project.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	// method for adding common data to response
	
	@ModelAttribute
	public void addCommonData(Model m , Principal p) {
		//Getting userName 
		String userName = p.getName();
		System.out.println("Username :"+userName);
		
		// Get the user object using username(Email)
		User user = userRepository.findByEmail(userName);
		m.addAttribute("user" , user);
	}
	
	@GetMapping("/index")
	public String home(Model model , Principal principal) {
		

		model.addAttribute("title","User Dashboard");
		
		return "user/user_dashboard";
	}
	
	
	// Add contact handler
	@GetMapping("/add-contact")
	public String addContact(Model model) {
		
		model.addAttribute("title" , "Add Contact");
		model.addAttribute("contact" , new Contact());
		
		return "user/add_contact";
	}
	
}
