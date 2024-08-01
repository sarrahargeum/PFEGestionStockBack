package com.example.stock.service;

import java.util.List;

import com.example.stock.dto.FournisseurDto;
import com.example.stock.model.Fournisseur;

public interface FournisseurService {
	
	FournisseurDto ajouterFournisseur(FournisseurDto dto);
	 void  updateFournisseur(Integer id, Fournisseur fournisseur);
	Fournisseur deleteFournisseur(Integer id);
    List<FournisseurDto> findAll();
    
    public FournisseurDto retrieveFournisseur(Integer id);

}
