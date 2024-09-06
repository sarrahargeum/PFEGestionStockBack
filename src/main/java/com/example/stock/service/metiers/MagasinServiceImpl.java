package com.example.stock.service.metiers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.model.Magasin;
import com.example.stock.repository.MagasinRepository;
import com.example.stock.service.MagasinService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MagasinServiceImpl implements MagasinService {
	
	@Autowired
    MagasinRepository magasinRepository;
	
	@Override
	public Magasin ajouterMagasin(Magasin mg) {
			return	magasinRepository.save(mg);
    }
	
		
	public void updateMagasin(Integer id, Magasin magasin) {
	   
		Optional<Magasin> mgInfo = magasinRepository.findById(id);

	    if (mgInfo.isPresent()) {
	        Magasin mg = mgInfo.get();
	        mg.setDescription(magasin.getDescription()); 
	        mg.setNom(magasin.getNom()); 
	        mg.setEmail(magasin.getEmail());
	        mg.setNumTel(magasin.getNumTel());
	        mg.setSiteWeb(magasin.getSiteWeb());

	        magasinRepository.save(mg); 
	    } 
	
	  }
	
	   public Magasin retrieveMagasin (Integer magasinId){
		   if (magasinRepository == null) {
	            log.error("Magasin ID is null");
	            return null;
	        }
	        return magasinRepository.findById(magasinId).orElseThrow(() ->
	        new EntityNotFoundException(
	            "Aucun MAgasin avec l'ID = " + magasinId + " n' ete trouve dans la BDD")
	    );
	    }

	@Override
	public Magasin deleteMagasin(Integer id) {
		 magasinRepository.deleteById(id);
	        return null;	}

	@Override
	public List<Magasin> findAll() {
		return magasinRepository.findAll().stream()
		        .collect(Collectors.toList());
    	    }	

	
 

  
}
