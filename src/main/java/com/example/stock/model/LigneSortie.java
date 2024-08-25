package com.example.stock.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ligneSortie")
public class LigneSortie {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantite;

    private Float prixUnitaire;
  
  
    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;

    @Column(name = "idmagasin")
    private Integer idMagasin;

   
    @ManyToOne
    @JoinColumn(name = "idbonSortie")
    private BonSortie bonSortie;
    
    

}
