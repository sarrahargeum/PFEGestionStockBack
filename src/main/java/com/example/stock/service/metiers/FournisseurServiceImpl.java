package com.example.stock.service.metiers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.model.Category;
import com.example.stock.model.Fournisseur;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.repository.FournisseurRepository;
import com.example.stock.service.FournisseurService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class FournisseurServiceImpl implements FournisseurService {
	
	@Autowired
	FournisseurRepository fournisseurRepository;
	
	@Override
	public Fournisseur ajouterFournisseur(Fournisseur four) {
		// TODO Auto-generated method stub
		 return fournisseurRepository.save(four);
	}

	@Override
	public Fournisseur updateFournisseur(Fournisseur fournisseur) {
		// TODO Auto-generated method stub
		fournisseurRepository.findById(fournisseur.getId());
        return fournisseurRepository.save(fournisseur);
	}

	@Override
	public Fournisseur deleteFournisseur(Integer id) {
		// TODO Auto-generated method stub
		fournisseurRepository.deleteById(id);
        return null;
	}

	@Override
	public List<Fournisseur> findAll() {
		// TODO Auto-generated method stub
        return fournisseurRepository.findAll().stream().collect(Collectors.toList());
	}
	
	
	

}
