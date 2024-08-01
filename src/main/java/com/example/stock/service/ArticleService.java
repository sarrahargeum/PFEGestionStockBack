package com.example.stock.service;

import com.example.stock.dto.ArticleDto;
import com.example.stock.dto.LigneEntreeDto;
import com.example.stock.dto.LigneSortieDto;
import com.example.stock.model.Article;
import com.example.stock.model.Category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {


    Article findById(Integer id);

   // ArticleDto findByCodeArticle(String codeArticle);
    
   
   void update(Integer id, Article Article);
   Article deleteArticle(Integer id);
    
     Article retrieveArticle (Integer id);
	

	List<Article> getAllArticle();

	  List<LigneSortieDto> findHistoriaueCommandeClient(Integer idArticle);

	  List<LigneEntreeDto> findHistoriqueCommandeFournisseur(Integer idArticle);

	  List<ArticleDto> findAllArticleByIdCategory(Integer idCategory);
    
 
}
