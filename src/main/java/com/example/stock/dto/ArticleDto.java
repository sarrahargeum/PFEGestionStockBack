package com.example.stock.dto;

import com.example.stock.model.Article;
import com.example.stock.model.Magasin;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ArticleDto {

	 private Integer id;
	    private String code;
	    private String designation;
	    private Float prix;
	    private Float tauxTva;
	 
	    private String image;

  private CategoryDto category;

  private MagasinDto magasin;

  public static ArticleDto fromEntity(Article article) {
    if (article == null) {
      return null;
    }
    return ArticleDto.builder()
        .id(article.getId())
        .code(article.getCode())
        .designation(article.getDesignation())
        .image(article.getImage())
        .prix(article.getPrix())
        .tauxTva(article.getTauxTva())
        .magasin(MagasinDto.fromEntity(article.getMagasin())) 
        .category(CategoryDto.fromEntity(article.getCategory()))
        .build();
  }

  public static Article toEntity(ArticleDto articleDto) {
    if (articleDto == null) {
      return null;
    }
    Article article = new Article();
    article.setId(articleDto.getId());
    article.setCode(articleDto.getCode());
    article.setDesignation(articleDto.getDesignation());
    article.setImage(articleDto.getImage());
    article.setPrix(articleDto.getPrix());
    article.setTauxTva(articleDto.getTauxTva());
    article.setMagasin(MagasinDto.toEntity(articleDto.getMagasin()));
    article.setCategory(CategoryDto.toEntity(articleDto.getCategory()));
    return article;
  }

}
