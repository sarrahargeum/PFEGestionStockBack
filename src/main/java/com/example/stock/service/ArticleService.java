package com.example.stock.service;

import com.example.stock.dto.ArticleDto;
import com.example.stock.dto.LigneEntreeDto;
import com.example.stock.dto.LigneSortieDto;
import com.example.stock.model.Article;



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
