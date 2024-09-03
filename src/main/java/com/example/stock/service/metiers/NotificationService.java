package com.example.stock.service.metiers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.model.Notification;
import com.example.stock.repository.NotificationRepository;

@Service
public class NotificationService implements com.example.stock.service.NotificationService {

    @Autowired
     NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
