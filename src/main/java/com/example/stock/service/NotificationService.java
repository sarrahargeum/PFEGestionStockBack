package com.example.stock.service;

import java.util.List;


import com.example.stock.model.Notification;


public interface NotificationService {
    List<Notification> getAllNotifications();
    Notification save(Notification notification);


}
