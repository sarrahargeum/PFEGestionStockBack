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
@Table(name = "ligneSortie")
public class LigneSortie {


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

    @Column(name = "idmagasin")
    private Integer idMagasin;

   
    @ManyToOne
    @JoinColumn(name = "bon_sortie_id")
    private BonSortie bonSortie;
    
    
    public static LigneSortie toEntity(LigneSortie dto) {
        if (dto == null) {
          return null;
        }
        LigneSortie ligneSortie = new LigneSortie();
        ligneSortie.setId(dto.getId());
        ligneSortie.setQuantite(dto.getQuantite());
        ligneSortie.setPrixUnitaire(dto.getPrixUnitaire());
        ligneSortie.setArticle(dto.getArticle());
        ligneSortie.setBonSortie(dto.getBonSortie());
        ligneSortie.setIdMagasin(dto.getIdMagasin());
        ligneSortie.setEtatCommande(dto.getEtatCommande());
        return ligneSortie;
      }
}
