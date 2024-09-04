package com.example.stock.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;
	  
	  private String message;

	    @Column(name = "date_notification", nullable = false)
	    private Instant dateNotification;

	    @Column(name = "etat_notification", nullable = false)
	    private Boolean etatNotification;
	    
	     private String Type;
	    @Column(name = "code_commande")
	    private String codeCommande;

}
