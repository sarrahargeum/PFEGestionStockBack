package com.example.stock.service;

import java.util.List;

import com.example.stock.dto.BonEntreeDto;
import com.example.stock.dto.LigneEntreeDto;
import com.example.stock.model.BonEntree;
import com.example.stock.model.EtatCommande;

public interface BonEntreeService {
	
	BonEntreeDto save(BonEntreeDto BEntree);
	BonEntreeDto findById(Integer id);

	BonEntreeDto findByCode(String code);
	
	 void delete(Integer id);
	 
	  List<BonEntreeDto> findAll();
	  
	  
	  BonEntreeDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

	  BonEntreeDto updateQuantiteCommande(Integer idCommande, Integer idligneEntreeFournisseur, Integer quantite);
	   
	   BonEntreeDto updateFournisseur(Integer idCommande, Integer idFournisseur);
	   
	   BonEntreeDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle);
	
	   // Delete article ==> delete LigneCommandeFournisseur
	   BonEntreeDto deleteArticle(Integer idCommande, Integer idLigneCommande);
	   
	   List<LigneEntreeDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande);
	BonEntreeDto deleteBonEntree(Integer id);

}
