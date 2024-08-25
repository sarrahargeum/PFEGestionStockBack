package com.example.stock.model;

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
    
   /* @Enumerated(EnumType.STRING)
    private EtatCommande etatCommande;
*/
    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idbonEntree",  nullable = false)
    private BonEntree bonEntree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id")  // Ensure this matches your database column
    private Fournisseur fournisseur;

    @Column(name = "idmagasin")
    private Integer idMagasin;

  
    

    

}
