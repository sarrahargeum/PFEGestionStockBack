package com.example.stock.repository;

import com.example.stock.model.LigneEntreeFournisseur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneEntreeFournisseurRepository extends JpaRepository<LigneEntreeFournisseur, Integer> {
	
    List<LigneEntreeFournisseur> findAllByBonEntreFournisseurId(Integer bonEntreFournisseurId);

	  List<LigneEntreeFournisseur> findAllByArticleId(Integer idCommande);
}
