package com.spring.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spring.project.entities.User;
import com.spring.project.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		// fetching user from database
		
		User user = userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UsernameNotFoundException("Could not found user!");
		}
		else {
			CustomUserDetails customUserDetails = new CustomUserDetails(user);	
			return customUserDetails;
		}
	}
}
