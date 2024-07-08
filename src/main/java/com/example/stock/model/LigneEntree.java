package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "ligneEntree")
public class LigneEntree {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantite;
    private Float prixUnitaire;
    
    @Enumerated(EnumType.STRING)
    private EtatCommande etatCommande;

    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "idbonEntree")
    private BonEntree bonEntree;

    @Column(name = "idmagasin")
    private Integer idMagasin;

  

}
