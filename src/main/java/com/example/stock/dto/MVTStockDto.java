package com.example.stock.dto;

import java.time.Instant;

import com.example.stock.model.MVTStock;
import com.example.stock.model.TypeStock;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MVTStockDto {

	private Integer id;
    private Integer quantite;
    private Instant dateMvt;

  private ArticleDto article;

  private TypeStock typeMvt;


  private Integer idMagasin;

  public static MVTStockDto fromEntity(MVTStock mvtStk) {
    if (mvtStk == null) {
      return null;
    }

    return MVTStockDto.builder()
        .id(mvtStk.getId())
        .dateMvt(mvtStk.getDateMvt())
        .quantite(mvtStk.getQuantite())
        .article(ArticleDto.fromEntity(mvtStk.getArticle()))
        .typeMvt(mvtStk.getTypestock())
        .idMagasin(mvtStk.getIdMagasin())
        .build();
  }

  public static MVTStock toEntity(MVTStockDto dto) {
    if (dto == null) {
      return null;
    }

    MVTStock mvtStk = new MVTStock();
    mvtStk.setId(dto.getId());
    mvtStk.setQuantite(dto.getQuantite());
    mvtStk.setDateMvt(dto.getDateMvt());
    mvtStk.setTypestock(dto.getTypeMvt());
    mvtStk.setIdMagasin(dto.getIdMagasin());
    mvtStk.setArticle(ArticleDto.toEntity(dto.getArticle()));
   
    return mvtStk;
  }
}
