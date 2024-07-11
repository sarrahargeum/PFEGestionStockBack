package com.example.stock.dto;

import com.example.stock.model.LigneSortie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LigneSortieDto {

  private Integer id;

  private ArticleDto article;

  @JsonIgnore
  private BonSortieDto commandeClient;

  private Integer quantite;

  private Float prixUnitaire;

  private Integer idMagasin;

  public static LigneSortieDto fromEntity(LigneSortie ligneCommandeClient) {
    if (ligneCommandeClient == null) {
      return null;
    }
    return LigneSortieDto.builder()
        .id(ligneCommandeClient.getId())
        .article(ArticleDto.fromEntity(ligneCommandeClient.getArticle()))
        .quantite(ligneCommandeClient.getQuantite())
        .prixUnitaire(ligneCommandeClient.getPrixUnitaire())
        .idMagasin(ligneCommandeClient.getIdMagasin())
        .build();
  }

  public static LigneSortie toEntity(LigneSortieDto dto) {
    if (dto == null) {
      return null;
    }

    LigneSortie ligneCommandeClient = new LigneSortie();
    ligneCommandeClient.setId(dto.getId());
    ligneCommandeClient.setArticle(ArticleDto.toEntity(dto.getArticle()));
    ligneCommandeClient.setPrixUnitaire(dto.getPrixUnitaire());
    ligneCommandeClient.setQuantite(dto.getQuantite());
    ligneCommandeClient.setIdMagasin(dto.getIdMagasin());
    return ligneCommandeClient;
  }

}
