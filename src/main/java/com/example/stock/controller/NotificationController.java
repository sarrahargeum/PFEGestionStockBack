package com.example.stock.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stock.model.Notification;
import com.example.stock.model.User;
import com.example.stock.repository.NotificationRepository;
import com.example.stock.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/notofication")
public class NotificationController {
	
    @Autowired
    NotificationRepository notificationRepository;
	

    @Autowired
    UserRepository userRepository;

    @MessageMapping("/notification")
    @SendTo("/topic/notifications")
    public Notification sendNotification(Notification notification) {
        return notification;
    }

    public void notifyOrderValidated(Integer orderId,Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setType("Vente"); // or "Achat" depending on the context
        notification.setMessage("Order " + orderId + " needs validation.");
        notification.setDateNotification(new Date());
        notification.setDestinataire(user);

        notificationRepository.save(notification);
    }
}
