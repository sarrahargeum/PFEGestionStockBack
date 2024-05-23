package com.example.stock.service;

import java.util.List;

import com.example.stock.model.Category;
import com.example.stock.model.Fournisseur;

public interface FournisseurService {
	
	Fournisseur ajouterFournisseur(Fournisseur four);
	Fournisseur updateFournisseur(Fournisseur fournisseur);
	Fournisseur deleteFournisseur(Integer id);
    List<Fournisseur> findAll();
}
