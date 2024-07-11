package com.example.stock.dto;
import com.example.stock.model.Magasin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MagasinDto {

	  private Integer id;
	    private  String nom;
	    private  String description;
	    private  String email;
	    private  String numTel;
	    private String siteWeb;



  @JsonIgnore
  private List<UserDto> utilisateurs;

  public static MagasinDto fromEntity(Magasin magasin) {
    if (magasin == null) {
      return null;
    }
    return MagasinDto.builder()
        .id(magasin.getId())
        .nom(magasin.getNom())
        .email(magasin.getEmail())
        .description(magasin.getDescription())
        .numTel(magasin.getNumTel())
        .siteWeb(magasin.getSiteWeb())
        .build();
  }

  public static Magasin toEntity(MagasinDto dto) {
    if (dto == null) {
      return null;
    }
    Magasin entreprise = new Magasin();
    entreprise.setId(dto.getId());
    entreprise.setNom(dto.getNom());
    entreprise.setDescription(dto.getDescription());
    entreprise.setEmail(dto.getEmail());
    entreprise.setNumTel(dto.getNumTel());
    entreprise.setSiteWeb(dto.getSiteWeb());

    return entreprise;
  }

}
