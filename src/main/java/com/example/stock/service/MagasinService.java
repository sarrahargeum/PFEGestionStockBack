package com.example.stock.service;

import java.util.List;

import com.example.stock.model.Category;
import com.example.stock.model.Magasin;

public interface MagasinService {
	
	Magasin ajouterMagasin(Magasin mg);
	Magasin updateMagasin(Magasin Magasin);
	Magasin deleteMagasin(Integer id);
    List<Magasin> findAll();

}
