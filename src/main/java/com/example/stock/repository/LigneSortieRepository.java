package com.example.stock.repository;

import com.example.stock.model.Article;
import com.example.stock.model.BonSortie;
import com.example.stock.model.LigneEntree;
import com.example.stock.model.LigneSortie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface LigneSortieRepository extends JpaRepository<LigneSortie, Integer> {
	
	 List<LigneSortie> findAllByBonSortieId(Integer bonSortieId);

	  List<LigneSortie> findAllByArticleId(Integer idCommande);
	  
	    void deleteByBonSortie(BonSortie bonSortie); 

	    void deleteByArticle(Article article);
	    @Query("SELECT bs FROM BonSortie bs LEFT JOIN FETCH bs.ligneSorties WHERE bs.id = :id")
	    Optional<BonSortie> findByIdWithLigneSorties(@Param("id") Integer id);


}
