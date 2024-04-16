package com.spring.project.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.project.entities.User;
import com.spring.project.helper.Message;
import com.spring.project.repository.UserRepository;
import com.spring.project.service.EmailService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ForgotController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	// email id form open handler
	@GetMapping("/forgotPassword")
	public String forgotPassword() {
		
		return "forgot_email_form";
	}
	
	// send otp handler
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email , HttpSession session) {
		
		User user = this.userRepository.findByEmail(email); 
		if (user == null) {
			session.setAttribute("message", new Message("Wrong email" , "wrong_email"));
			return "forgot_email_form";
		}
		else {
			session.setAttribute("message",new Message("OTP send successfully","success"));
			System.out.println("Email :"+email);
			
			
			// generating otp of 4 digit
			Random random = new Random();
			
			int otp = 1000 + random.nextInt(9000);
			System.out.println("OTP :"+otp);
			
			// write a code for send otp to email
			
			String subject = "OTP from Smart Contact Manager Application";
			String message = "OTP = "+otp;
			String to = email;
			
			boolean flag = this.emailService.sendEmail(subject, message, to);
			
			if (flag) {
				
				session.setAttribute("myotp", otp);
				session.setAttribute("email", email);
				
				return "verify_otp";
			}
			else {
				return "forgot_email_form";
			}
		}
	}
	
	
	// verify OTP handler
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp") int otp , HttpSession session) {
		
		int myOTP = (int) session.getAttribute("myotp");
		
		if(otp == myOTP) {
			// password change form
			return "password_change_form";
		}
		else {
			session.setAttribute("message", new Message("please enter valid otp" , "not_matched"));
			return "verify_otp";
		}
	}
	
	@PostMapping("/change_password")	
	public String changePassword(@RequestParam("password") String password,
								@RequestParam("confirmPassword") String confirmPassword , HttpSession session) {
		
		
		if (!password.equals(confirmPassword)) {
			
			session.setAttribute("message", new Message("password not matched","password"));
			return "password_change_form";
			
		}
		else {
			
			String email = (String) session.getAttribute("email");
			User user = this.userRepository.findByEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			this.userRepository.save(user);
			session.setAttribute("message", new Message("password is changed","changed"));
			return "login";
		}
		
		
		
	}
	
	
}
