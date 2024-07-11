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

  
    
    public static LigneEntree toEntity(LigneEntree dto) {
        if (dto == null) {
          return null;
        }
        LigneEntree ligneEntree = new LigneEntree();
        ligneEntree.setId(dto.getId());
        ligneEntree.setQuantite(dto.getQuantite());
        ligneEntree.setPrixUnitaire(dto.getPrixUnitaire());
        ligneEntree.setArticle(dto.getArticle());
        ligneEntree.setBonEntree(dto.getBonEntree());
        ligneEntree.setIdMagasin(dto.getIdMagasin());
        ligneEntree.setEtatCommande(dto.getEtatCommande());
        return ligneEntree;
      }
    

}
