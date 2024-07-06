package com.example.stock.service.metiers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.controller.NotificationController;
import com.example.stock.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	  @Autowired
	    NotificationController notificationController;

	    public void validateOrder(Integer orderId, Integer userId) {
	        // Logic to validate order and update order status
	    	
	        // Send WebSocket notification
	        notificationController.notifyOrderValidated(orderId , userId);
	    }

}
