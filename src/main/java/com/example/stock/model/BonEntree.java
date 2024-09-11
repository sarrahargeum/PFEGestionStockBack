package com.example.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bonEntree")
public class BonEntree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String code;


    private Instant dateCommande;
    
    @PrePersist
    public void generateCodeProp() {
        String prefix = "C";  // Define the prefix for the code
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/StockMnager", "root", "");
             PreparedStatement ps = connection.prepareStatement("SELECT COALESCE(MAX(id), 0) + 1 FROM bon_entree")) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long nextVal = rs.getLong(1);  // Get the next available number based on the ID
                this.code = prefix + String.format("%03d", nextVal);  // Generate code like "C001", "C002", etc.
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error generating code for BonEntree", e);
        }
    }
 

    @Column(name = "etatcommande")
    @Enumerated(EnumType.STRING)
    private EtatCommande etatCommande;

    @Column(name = "idmagasin")
    private Integer idMagasin;
  
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "idfournisseur")
    private Fournisseur fournisseur;

 
    @OneToMany(mappedBy = "bonEntree", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LigneEntree> ligneEntrees;
    
}
