package com.spring.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
public boolean sendEmail(String subject , String message, String to) {
	
	try {
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setFrom("abc@gmail.com");
		mailMessage.setTo(to);
		mailMessage.setText(message);
		mailMessage.setSubject(subject);
		
		javaMailSender.send(mailMessage);
		
		System.out.println("Mail Send....");
		
		return true;
		
	} catch (Exception e) {
		
		System.out.println("Failed To send Email : " + to);
		
		e.printStackTrace();
		
		return false;
	}
	}
}
