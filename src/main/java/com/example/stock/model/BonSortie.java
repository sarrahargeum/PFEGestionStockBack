package com.example.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "idclient")
    private Client client;
    
@OneToMany(mappedBy = "bonSortie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LigneSortie> ligneSorties;
    
   
    
    
  

}
