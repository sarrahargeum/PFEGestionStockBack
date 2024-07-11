package com.example.stock.dto;

import com.example.stock.model.Fournisseur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FournisseurDto {
	 private Integer id;

	    private String nom;

	    private String prenom;
	    private String adresse;
	    private String mail;
	    private String numTel;
	    private Integer idMagasin;


  @JsonIgnore
  private List<BonEntreeDto> bonEntrees;

  public static FournisseurDto fromEntity(Fournisseur fournisseur) {
    if (fournisseur == null) {
      return null;
    }
    return FournisseurDto.builder()
        .id(fournisseur.getId())
        .nom(fournisseur.getNom())
        .prenom(fournisseur.getPrenom())
        .adresse(fournisseur.getAdresse())
        .mail(fournisseur.getMail())
        .numTel(fournisseur.getNumTel())
        .idMagasin(fournisseur.getIdMagasin())
        .build();
  }

  public static Fournisseur toEntity(FournisseurDto dto) {
    if (dto == null) {
      return null;
    }
    Fournisseur fournisseur = new Fournisseur();
    fournisseur.setId(dto.getId());
    fournisseur.setNom(dto.getNom());
    fournisseur.setPrenom(dto.getPrenom());
    fournisseur.setAdresse(dto.getAdresse());
    fournisseur.setMail(dto.getMail());
    fournisseur.setNumTel(dto.getNumTel());
    fournisseur.setIdMagasin(dto.getIdMagasin());

    return fournisseur;
  }
}
