package com.example.stock.model;

import java.time.Instant;

import com.example.stock.dto.ClientDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
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
	    
	    @Column(name = "type", nullable = false) // Ensure this is correct
	     private String type;
	    @Column(name = "code_commande")
	    private String codeCommande;

	    public static Notification fromEntity(Notification notification) {
	        if (notification == null) {
	          return null;
	        }
	        return Notification.builder()
	            .id(notification.getId())
	            .codeCommande(notification.getCodeCommande())
	            .dateNotification(notification.getDateNotification())
	            .etatNotification(notification.getEtatNotification())
	            .message(notification.getMessage())
	            .type(notification.getType())
	            .build();
	      }

		public boolean isEtatNotification() {
			// TODO Auto-generated method stub
			return false;
		}
	    
}
