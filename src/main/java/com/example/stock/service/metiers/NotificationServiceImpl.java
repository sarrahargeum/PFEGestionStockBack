package com.example.stock.service.metiers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.model.Notification;
import com.example.stock.repository.NotificationRepository;
import com.example.stock.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
     NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
        
    }
    
    public List<Notification> getNotificationsByType(String type) {
        return notificationRepository.findByType(type);
    }
    
    
    public List<Notification> markAllAsRead() {
        List<Notification> unreadNotifications = notificationRepository.findByEtatNotificationFalse();

        unreadNotifications.forEach(notification -> {
            notification.setEtatNotification(true);
        });

        return notificationRepository.saveAll(unreadNotifications);
    }



}
