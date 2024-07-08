package com.example.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bonSortie")
public class BonSortie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private Instant dateCommande;

 
    
    @Column(name = "etatcommande")
    @Enumerated(EnumType.STRING)
    private EtatCommande etatCommande;

    @Column(name = "idmagasin")
    private Integer idMagasin;
    
    @ManyToOne
    @JoinColumn(name = "idclient")
    private Client client;
    
    @OneToMany(mappedBy = "bonSortie", cascade = CascadeType.ALL)
    private Set<LigneSortie> ligneSorties;
    
    public boolean isBonSortieLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
      }
    
    
  

}
