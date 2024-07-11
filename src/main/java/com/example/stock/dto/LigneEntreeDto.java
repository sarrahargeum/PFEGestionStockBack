package com.example.stock.dto;


import com.example.stock.model.BonEntree;
import com.example.stock.model.LigneEntree;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LigneEntreeDto {

  private Integer id;

  private ArticleDto article;

  private BonEntree bonEntree;

  private Integer quantite;

  private Float prixUnitaire;

  private Integer idMagasin;

  public static LigneEntreeDto fromEntity(LigneEntree ligneCommandeFournisseur) {
    if (ligneCommandeFournisseur == null) {
      return null;
    }
    return LigneEntreeDto.builder()
        .id(ligneCommandeFournisseur.getId())
        .article(ArticleDto.fromEntity(ligneCommandeFournisseur.getArticle()))
        .quantite(ligneCommandeFournisseur.getQuantite())
        .prixUnitaire(ligneCommandeFournisseur.getPrixUnitaire())
        .idMagasin(ligneCommandeFournisseur.getIdMagasin())
        .build();
  }

  public static LigneEntree toEntity(LigneEntreeDto dto) {
    if (dto == null) {
      return null;
    }

    LigneEntree ligneCommandeFournisseur = new LigneEntree();
    ligneCommandeFournisseur.setId(dto.getId());
    ligneCommandeFournisseur.setArticle(ArticleDto.toEntity(dto.getArticle()));
    ligneCommandeFournisseur.setPrixUnitaire(dto.getPrixUnitaire());
    ligneCommandeFournisseur.setQuantite(dto.getQuantite());
    ligneCommandeFournisseur.setIdMagasin(dto.getIdMagasin());
    return ligneCommandeFournisseur;
  }

}
