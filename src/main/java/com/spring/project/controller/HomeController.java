package com.spring.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.project.entities.User;
import com.spring.project.helper.Message;
import com.spring.project.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	// Handler for Home Page
	
	@GetMapping("/")
	public String home(Model model) {
		
		model.addAttribute("title" , "Home-Smart Contact Manager");
			
		return "home";
	}
	
	// Handler for About Page
	
	@GetMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title" , "About-Smart Contact Manager");
		
		return "about";
	}
	
	// Handler for login Page
	
	@GetMapping("/login")
	public String login(Model model) {
		
		model.addAttribute("title" , "Login-Smart Contact Manager");
		
		return "login";
	}
	
	// Handler for Registration Page
	
	@GetMapping("/signup")
	public String signUp(Model model) {
		
		model.addAttribute("title" , "Register-Smart Contact Manager");
		model.addAttribute("user" , new User());
		
		return "signup";
	}
	
//	Handler for Register User submit data
	
	@PostMapping("/submit_data")
	public String submitData(@Valid 
							@ModelAttribute("user") User user ,  BindingResult result ,
							@RequestParam(value = "agreed" , defaultValue = "false") boolean agreed  ,
							Model model ,HttpSession session) {
		
		try {
			
			if(!agreed) {
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			if (result.hasErrors()) {
				model.addAttribute("user" , user);
				return "signup";
			}
			
			user.setRole("USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			userRepository.save(user);
			
			model.addAttribute("user" , new User());
			
			session.setAttribute("message", new Message("Successfully Registered" , "alert-success"));
//			return "login";
			
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user" , new User());
			session.setAttribute("message", new Message("Something Went wrong !!", "alert-danger"));
			return "signup";
		}
		return "login";
		
	}
	
	
}
