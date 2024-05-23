package com.example.stock.service;

import com.example.stock.model.Article;
import com.example.stock.model.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {

/*	public void  ajoutArticle(MultipartFile image, String code, String designation
            , float prix , float tauxTva  ,Integer categoryId, Integer magasinId);*/
	
   // public Article  ajoutArticle(String image, String code, String designation
         //   , float prix , float tauxTva  );
    Article findById(Integer id);

    Article findByCodeArticle(String codeArticle);
    
    Article updateArticle(Article article);
    
    Article deleteArticle(Integer id);
    
  
	Article ajoutArticle(MultipartFile image);

	List<Article> findAll();

	//ResponseEntity<List<String>> getAll1();
    
 
}
