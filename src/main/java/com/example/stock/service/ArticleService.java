package com.example.stock.service;

import com.example.stock.model.Article;
import com.example.stock.model.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {


    Article findById(Integer id);

    Article findByCodeArticle(String codeArticle);
    
   
   void update(Integer id, Article Article);
    Article deleteArticle(Integer id);
    
    public Article retrieveArticle (Integer id);
	

	List<Article> getAllArticle();

    
 
}
