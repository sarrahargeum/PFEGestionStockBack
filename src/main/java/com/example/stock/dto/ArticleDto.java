package com.example.stock.dto;

import com.example.stock.model.Article;
import com.example.stock.model.Magasin;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)

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
	private Long stockDisponible;

	
	public static ArticleDto fromEntity(Article article) {
		if (article == null) {
			return null;
		}
		return ArticleDto.builder().id(article.getId()).code(article.getCode()).designation(article.getDesignation())
				.image(article.getImage()).prix(article.getPrix()).tauxTva(article.getTauxTva())
				.magasin(MagasinDto.fromEntity(article.getMagasin()))
				.category(CategoryDto.fromEntity(article.getCategory()))
				//.stockDisponible(article.getStocks().size())
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

	public ArticleDto(Integer id, String code, String designation, Float prix, Float tauxTva, String image,
			Long stockDisponible) {
		super();
		this.id = id;
		this.code = code;
		this.designation = designation;
		this.prix = prix;
		this.tauxTva = tauxTva;
		this.image = image;
		this.stockDisponible = stockDisponible;
	}

	public ArticleDto(Integer id, String code, String designation, Float prix, Float tauxTva, String image,
			CategoryDto category, MagasinDto magasin, Long stockDisponible) {
		super();
		this.id = id;
		this.code = code;
		this.designation = designation;
		this.prix = prix;
		this.tauxTva = tauxTva;
		this.image = image;
		this.category = category;
		this.magasin = magasin;
		this.stockDisponible = stockDisponible;
	}

	public ArticleDto() {
		super();
	}
	
}
