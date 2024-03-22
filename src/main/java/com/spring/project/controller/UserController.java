package com.spring.project.controller;

import java.io.File;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.project.entities.Contact;
import com.spring.project.entities.User;
import com.spring.project.repository.UserRepository;

import jakarta.persistence.criteria.Path;

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
	
	
	@PostMapping("/contact-process")
	public String processContact(@ModelAttribute Contact contact , 
									@RequestParam("profile_Image") MultipartFile file , Principal principal) {
		
		try {
			//Getting userName
			String userName = principal.getName();
			
			// Get the user object using username(Email)
			User user = userRepository.findByEmail(userName);
			
			if (file.isEmpty()) {
				// file is empty then database save default image
				System.out.println("File is empty!");
				contact.setImageUrl("contact.png");
			}
			else {
				// upload the file to folder and update the name to contact
				contact.setImageUrl(file.getOriginalFilename());
				File saveFile = new ClassPathResource("/static/images").getFile();
				
				
				
			}
			
			contact.setUser(user);
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			
			System.out.println("Added to database");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		
		return "user/add_contact";
	}
	
}
