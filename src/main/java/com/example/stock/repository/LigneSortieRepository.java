package com.example.stock.repository;

import com.example.stock.model.Article;
import com.example.stock.model.BonSortie;
import com.example.stock.model.LigneEntree;
import com.example.stock.model.LigneSortie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneSortieRepository extends JpaRepository<LigneSortie, Integer> {
	
	 List<LigneSortie> findAllByBonSortieId(Integer bonSortieId);

	  List<LigneSortie> findAllByArticleId(Integer idCommande);
	  
	    void deleteByBonSortie(BonSortie bonSortie); 

	    void deleteByArticle(Article article);

}
