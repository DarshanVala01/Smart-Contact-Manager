package com.spring.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class MyOrders {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long myOrderId;
	
	private String orderId;
	
	private int amount;
	
	private String receipt;
	
	private String status;
	
	private String paymentId;
	
	@ManyToOne
	private User user;
}		
