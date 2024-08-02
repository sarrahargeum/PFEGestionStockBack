package com.example.stock.repository;

import com.example.stock.model.LigneEntree;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface LigneEntreeRepository extends JpaRepository<LigneEntree, Integer> {
	
    List<LigneEntree> findAllByBonEntreeId(Integer bonEntreeId);

	  List<LigneEntree> findAllByArticleId(Integer idCommande);
	 // List<LigneEntree> findAllByCommandeFournisseurId(Integer idCommande);

	  
}
