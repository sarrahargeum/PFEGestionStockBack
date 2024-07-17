package com.example.stock.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.example.stock.model.User;
import com.example.stock.repository.NotificationRepository;
import com.example.stock.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class NotificationController {
	
   /* @Autowired
    NotificationRepository notificationRepository;
	

    @Autowired
    UserRepository userRepository;
    

    */
	  @Autowired
	    private SimpMessagingTemplate messagingTemplate;

	 @MessageMapping("/notify")
	    @SendTo("/topic/notifications")
	    public Notification notify(Notification notification) {
		 System.out.println("eli yji");
	        return new Notification(HtmlUtils.htmlEscape(notification.getMessage()));
	        
	    }

	  

	    public void sendNotification(String message) {
	        Notification notification = new Notification(message);
	        messagingTemplate.convertAndSend("/topic/notifications", notification);
	    }
	}

	class Notification {
	    private String message;

	    public Notification() {}

	    public Notification(String message) {
	        this.message = message;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }
	}
