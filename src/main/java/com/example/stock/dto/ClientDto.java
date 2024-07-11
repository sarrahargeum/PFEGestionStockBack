package com.example.stock.dto;

import com.example.stock.model.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDto {

	private Integer id;

    private String nom;

    private String prenom;
    private String adresse;
    private String numTel;

    private Integer idMagasin;

  @JsonIgnore
  private List<BonSortieDto> commandeClients;

  public static ClientDto fromEntity(Client client) {
    if (client == null) {
      return null;
    }
    return ClientDto.builder()
        .id(client.getId())
        .nom(client.getNom())
        .prenom(client.getPrenom())
        .adresse(client.getAdresse())
        .numTel(client.getNumTel())
        .idMagasin(client.getIdMagasin())
        .build();
  }

  public static Client toEntity(ClientDto dto) {
    if (dto == null) {
      return null;
    }
    Client client = new Client();
    client.setId(dto.getId());
    client.setNom(dto.getNom());
    client.setPrenom(dto.getPrenom());
    client.setAdresse(dto.getAdresse());
    client.setNumTel(dto.getNumTel());
    client.setIdMagasin(dto.getIdMagasin());
    return client;
  }

}
