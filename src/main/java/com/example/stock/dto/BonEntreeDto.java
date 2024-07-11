package com.example.stock.dto;

import com.example.stock.model.BonEntree;
import com.example.stock.model.EtatCommande;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BonEntreeDto {

  private Integer id;

  private String code;

  private Instant dateCommande;

  private EtatCommande etatCommande;

  private FournisseurDto fournisseur;

  private Integer idMagasin;

  private List<LigneEntreeDto> ligneEntrees;

  public static BonEntreeDto fromEntity(BonEntree commandeFournisseur) {
    if (commandeFournisseur == null) {
      return null;
    }
    return BonEntreeDto.builder()
        .id(commandeFournisseur.getId())
        .code(commandeFournisseur.getCode())
        .dateCommande(commandeFournisseur.getDateCommande())
        .fournisseur(FournisseurDto.fromEntity(commandeFournisseur.getFournisseur()))
        .etatCommande(commandeFournisseur.getEtatCommande())
        .idMagasin(commandeFournisseur.getIdMagasin())
        .build();
  }

  public static BonEntree toEntity(BonEntreeDto dto) {
    if (dto == null) {
      return null;
    }
    BonEntree commandeFournisseur = new BonEntree();
    commandeFournisseur.setId(dto.getId());
    commandeFournisseur.setCode(dto.getCode());
    commandeFournisseur.setDateCommande(dto.getDateCommande());
    commandeFournisseur.setFournisseur(FournisseurDto.toEntity(dto.getFournisseur()));
    commandeFournisseur.setIdMagasin(dto.getIdMagasin());
    commandeFournisseur.setEtatCommande(dto.getEtatCommande());
    return commandeFournisseur;
  }

  public boolean isCommandeLivree() {
    return EtatCommande.LIVREE.equals(this.etatCommande);
  }

}
