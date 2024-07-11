package com.example.stock.service;

import java.util.List;

import com.example.stock.dto.FournisseurDto;
import com.example.stock.model.Fournisseur;

public interface FournisseurService {
	
	Fournisseur ajouterFournisseur(Fournisseur four);
	 void  updateFournisseur(Integer id, Fournisseur fournisseur);
	Fournisseur deleteFournisseur(Integer id);
    List<FournisseurDto> findAll();
    Fournisseur retrieveFournisseur (Integer fournisseurId);

}
