package com.spring.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.project.entities.MyOrders;

public interface OrderRepository extends JpaRepository<MyOrders, Long>{
	
	public MyOrders findByOrderId(String orderId);
}
