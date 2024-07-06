package com.example.stock.service;

import com.example.stock.dto.ArticleDto;
import com.example.stock.model.Article;
import com.example.stock.model.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {


    ArticleDto findById(Integer id);

   // ArticleDto findByCodeArticle(String codeArticle);
    
   
   void update(Integer id, ArticleDto Article);
   ArticleDto deleteArticle(Integer id);
    
     ArticleDto retrieveArticle (Integer id);
	

	List<ArticleDto> getAllArticle();

    
 
}
