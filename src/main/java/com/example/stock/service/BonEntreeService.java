package com.example.stock.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.example.stock.dto.BonEntreeDto;
import com.example.stock.dto.LigneEntreeDto;
import com.example.stock.model.EtatCommande;

import jakarta.servlet.http.HttpServletResponse;

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
	  public void generateEcelBE(HttpServletResponse response) throws IOException;
	long countBonEntrees();
	Map<String, Object> getBonDataByMonth(); 
}
