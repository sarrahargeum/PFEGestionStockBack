package com.example.stock.service.metiers;

import java.util.List;
import java.util.Optional;
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
	public Fournisseur ajouterFournisseur(Fournisseur four ) {
		 return fournisseurRepository.save(four);
	}


	
	public void updateFournisseur(Integer id, Fournisseur fournisseur) {
	  
	    Optional<Fournisseur> forniInfo = fournisseurRepository.findById(id);

	    if (forniInfo.isPresent()) {
	        Fournisseur four = forniInfo.get();
	        four.setNom(fournisseur.getNom()); 
	        four.setMail(fournisseur.getMail()); 
	        four.setPrenom(fournisseur.getPrenom());
	        four.setAdresse(fournisseur.getAdresse());
	        four.setNumTel(fournisseur.getNumTel());

	        fournisseurRepository.save(four); 
	    } else {
	        System.out.println("Fournisseur with ID = " + id + " not found.");
	    }
	
	  }
	
	

	@Override
	public Fournisseur deleteFournisseur(Integer id) {
		fournisseurRepository.deleteById(id);
        return null;
	}

	@Override
	public List<Fournisseur> findAll() {
        return fournisseurRepository.findAll().stream().collect(Collectors.toList());
	}
	
    public Fournisseur retrieveFournisseur (Integer fournisseurId){
    	Fournisseur four = fournisseurRepository.findById(fournisseurId).get();
        return  four;
    }
	

}
