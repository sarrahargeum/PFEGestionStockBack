package com.example.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.stock.model.Notification;
import com.example.stock.service.NotificationService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/notifications")
public class NotifController {

    @Autowired
     NotificationService notificationService;

    @GetMapping
    @ResponseBody
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
    

    @GetMapping("/not")
    public List<Notification> getNotificationsByType(@RequestParam("type") String type) {
        return notificationService.getNotificationsByType(type);
    }
    
    @PutMapping("/mark-all-as-read")
    public ResponseEntity<List<Notification>> markAllAsRead() {
        List<Notification> updatedNotifications = notificationService.markAllAsRead();
        return ResponseEntity.ok(updatedNotifications);
    }
}