package com.example.stock.dto;


import com.example.stock.model.BonSortie;
import com.example.stock.model.EtatCommande;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BonSortieDto {

  private Integer id;

  private String code;

  private Instant dateCommande;

  private EtatCommande etatCommande;

  private ClientDto client;

  private Integer idMagasin;

  private List<LigneSortieDto> ligneSorties;

  public static BonSortieDto fromEntity(BonSortie commandeClient) {
    if (commandeClient == null) {
      return null;
    }
    return BonSortieDto.builder()
        .id(commandeClient.getId())
        .code(commandeClient.getCode())
        .dateCommande(commandeClient.getDateCommande())
        .etatCommande(commandeClient.getEtatCommande())
        .client(ClientDto.fromEntity(commandeClient.getClient()))
        .idMagasin(commandeClient.getIdMagasin())
        .build();

  }

  public static BonSortie toEntity(BonSortieDto dto) {
    if (dto == null) {
      return null;
    }
    BonSortie commandeClient = new BonSortie();
    commandeClient.setId(dto.getId());
    commandeClient.setCode(dto.getCode());
    commandeClient.setClient(ClientDto.toEntity(dto.getClient()));
    commandeClient.setDateCommande(dto.getDateCommande());
    commandeClient.setEtatCommande(dto.getEtatCommande());
    commandeClient.setIdMagasin(dto.getIdMagasin());
    return commandeClient;
  }

  public boolean isCommandeLivree() {
    return EtatCommande.VALIDEE.equals(this.etatCommande);
  }
}
