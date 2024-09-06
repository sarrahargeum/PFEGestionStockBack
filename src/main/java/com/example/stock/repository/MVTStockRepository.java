package com.example.stock.repository;

import com.example.stock.model.Article;
import com.example.stock.model.MVTStock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface MVTStockRepository extends JpaRepository<MVTStock, Integer> {

	
	

	  @Query("select sum(m.quantite) from MVTStock m where m.article.id = :idArticle")
	  Integer stockReelArticle(@Param("idArticle") Integer idArticle);

	  List<MVTStock> findAllByArticleId(Integer idArticle);
	    
	    void deleteByArticle(Article article);

}
