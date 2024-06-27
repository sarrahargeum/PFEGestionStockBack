package com.example.stock.service;

import java.util.List;

import com.example.stock.model.BonEntreFournisseur;
import com.example.stock.model.EtatCommande;

public interface BonEntreFournisseurService {
	
	BonEntreFournisseur save(BonEntreFournisseur bonentreFourni);
	BonEntreFournisseur findById(Integer id);

	BonEntreFournisseur findByCode(String code);
	
	 void delete(Integer id);
	 
	  List<BonEntreFournisseur> findAll();
	  
	  
	  BonEntreFournisseur updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

	   BonEntreFournisseur updateQuantiteCommande(Integer idCommande, Integer idligneEntreeFournisseur, Integer quantite);
}
