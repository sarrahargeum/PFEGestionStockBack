package com.example.stock.dto;

import com.example.stock.model.Article;
import com.example.stock.model.Category;
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
    private Category category;

    private Magasin magasin;
    
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
            .magasin(article.getMagasin())
            .category(article.getCategory())
            .build();
      }
}
