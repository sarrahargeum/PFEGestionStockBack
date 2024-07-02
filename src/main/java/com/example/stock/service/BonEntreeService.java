package com.example.stock.service;

import java.util.List;

import com.example.stock.model.BonEntree;
import com.example.stock.model.EtatCommande;

public interface BonEntreeService {
	
	BonEntree save(BonEntree bonentreFourni);
	BonEntree findById(Integer id);

	BonEntree findByCode(String code);
	
	 void delete(Integer id);
	 
	  List<BonEntree> findAll();
	  
	  
	  BonEntree updateEtatCommande(Integer idCommande, EtatCommande etatCommande);

	   BonEntree updateQuantiteCommande(Integer idCommande, Integer idligneEntreeFournisseur, Integer quantite);
}
