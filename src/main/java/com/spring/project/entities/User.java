package com.spring.project.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message = "Name can'''t empty")
	@Size(min = 3 , max = 15 , message = "Username must be between 3 - 15 character")
	private String name;
	
	@NotBlank(message = "Email can'''t empty")
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$" , message = "Invalid Email !!")
	@Column(unique = true)
	private String email;
	
	
	@NotBlank(message = "Password can'''t empty")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$" , message = "Please enter strong password !")
	private String password;
	
	
	private String imageUrl;
	
	@NotBlank(message = "Username can'''t empty")
	@Column(length = 500 )
	private String about;
	
	private String role;
	
	private boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();
	
}
