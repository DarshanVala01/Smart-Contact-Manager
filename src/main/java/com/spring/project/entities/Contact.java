package com.spring.project.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

	@Id
	@Column(name = "c_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cId;
	
	private String name;
	
	private int phoneNo;
	
	private String nickName;
	
	private String work;
	
	@Column(unique = true)
	private String email;
	
	@Lob
	private String imageUrl;
	
	@Column(length = 1000)
	private String description;
	
	@ManyToOne
	private User user;
	
}
