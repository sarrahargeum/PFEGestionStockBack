package com.example.stock.service;

import java.util.List;

import com.example.stock.model.BonEntree;
import com.example.stock.model.BonSortie;
import com.example.stock.model.EtatCommande;

public interface BonSortieService {
	
	
	BonSortie save(BonSortie bonSortie);
	BonSortie findById(Integer id);

	BonSortie findByCode(String code);
	
	 void delete(Integer id);
	 
	  List<BonSortie> findAll();
	  
	  
	  BonSortie updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

	  BonSortie updateQuantiteCommande(Integer idCommande, Integer idligneSortie, Integer quantite);
}
