package com.example.stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@CrossOrigin(origins = "*")
@RestController
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Handles incoming WebSocket messages for order validation notifications
    @MessageMapping("/order/validate")
    @SendTo("/topic/order/validation")
    public String handleOrderValidationMessage(String message) {
        return HtmlUtils.htmlEscape(message);
    }

    // Handles incoming WebSocket messages for stock notifications
    @MessageMapping("/stock/out-of-stock")
    @SendTo("/topic/stock/out-of-stock")
    public String handleOutOfStockMessage(String message) {
        return HtmlUtils.htmlEscape(message);
    }
    /**
     * hedhi mch 3aytelha ba3d mata3mel verif 3a stock wl9itou wfe
     */
    // Sends an out-of-stock notification to admin users
    public void sendOutOfStockNotification(String message) {
        messagingTemplate.convertAndSend("/topic/stock/out-of-stock", HtmlUtils.htmlEscape(message));
    }
    /**
     * w hedhi wa9et ta3mel save
     * 
     */
    // Sends a validation notification to chef magasinier
    public void sendOrderValidationNotification(String message) {
        messagingTemplate.convertAndSend("/topic/order/validation", HtmlUtils.htmlEscape(message));
    }
}

// Notification class to encapsulate notification messages
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
