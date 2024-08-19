package com.example.stock.service;

import java.util.List;

import com.example.stock.dto.BonSortieDto;
import com.example.stock.dto.LigneSortieDto;
import com.example.stock.model.BonEntree;
import com.example.stock.model.BonSortie;
import com.example.stock.model.EtatCommande;

public interface BonSortieService {
	
	
	BonSortieDto save(BonSortieDto bonSortie);
	BonSortieDto findById(Integer id);

	BonSortieDto findByCode(String code);
	
	 void delete(Integer id);
	 
	  List<BonSortieDto> findAll();
	  
	  
	  BonSortieDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

	  BonSortieDto updateQuantiteCommande(Integer idCommande, Integer idligneSortie, Integer quantite);
	  
	  BonSortieDto updateClient(Integer idCommande, Integer idClient);

	  BonSortieDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

	  // Delete article ==> delete LigneCommandeClient
	  BonSortieDto deleteArticle(Integer idCommande, Integer idLigneCommande);
	  
	  List<LigneSortieDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande);

}
