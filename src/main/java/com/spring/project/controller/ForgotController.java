package com.spring.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForgotController {
	
	@GetMapping("/forgotPassword")
	public String forgotPassword() {
		
		return "forgot_email_form";
	}
	
}
