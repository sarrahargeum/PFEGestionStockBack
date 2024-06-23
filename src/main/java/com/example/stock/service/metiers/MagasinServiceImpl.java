package com.example.stock.service.metiers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.model.Category;
import com.example.stock.model.Fournisseur;
import com.example.stock.model.Magasin;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.repository.MagasinRepository;
import com.example.stock.service.ArticleService;
import com.example.stock.service.MagasinService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MagasinServiceImpl implements MagasinService {
	
	@Autowired
    MagasinRepository magasinRepository;
	
	@Override
	public Magasin ajouterMagasin(Magasin mg) {
		// TODO Auto-generated method stub
		return magasinRepository.save(mg);
	}

	
	public void updateMagasin(Integer id, Magasin magasin) {
	   
		Optional<Magasin> mgInfo = magasinRepository.findById(id);

	    if (mgInfo.isPresent()) {
	        Magasin mg = mgInfo.get();
	        mg.setDescription(magasin.getDescription()); 
	        mg.setEmail(magasin.getEmail());
	        mg.setNumTel(magasin.getNumTel());

	        magasinRepository.save(mg); 
	    } 
	
	  }
	
	   public Magasin retrieveMagasin (Integer magasinId){
		   Magasin mg = magasinRepository.findById(magasinId).get();
	        return  mg;
	    }

	@Override
	public Magasin deleteMagasin(Integer id) {
		// TODO Auto-generated method stub
		 magasinRepository.deleteById(id);
	        return null;	}

	@Override
	public List<Magasin> findAll() {
		// TODO Auto-generated method stub
        return magasinRepository.findAll().stream().collect(Collectors.toList());
	}

	
 

  
}
