package com.example.stock.repository;

import com.example.stock.model.Article;
import com.example.stock.model.BonEntree;
import com.example.stock.model.BonSortie;
import com.example.stock.model.Fournisseur;
import com.example.stock.model.LigneEntree;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface LigneEntreeRepository extends JpaRepository<LigneEntree, Integer> {
	
    List<LigneEntree> findAllByBonEntreeId(Integer bonEntreeId);

	  List<LigneEntree> findAllByArticleId(Integer idCommande);
	    void deleteByArticle(Article article);

	   
		    void deleteByBonEntree(BonEntree bonEntree); 

	  
}
