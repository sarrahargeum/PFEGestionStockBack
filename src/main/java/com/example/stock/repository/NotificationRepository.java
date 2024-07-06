package com.example.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stock.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{

}
