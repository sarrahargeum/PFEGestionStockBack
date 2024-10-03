package com.example.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.stock.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByEtatNotificationFalse();


    @Query("SELECT n FROM Notification n WHERE n.type = :type ORDER BY n.codeCommande DESC")
    List<Notification> findByType(@Param("type") String type);
}
