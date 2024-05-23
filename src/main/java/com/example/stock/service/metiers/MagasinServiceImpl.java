package com.example.stock.service.metiers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.model.Category;
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

	@Override
	public Magasin updateMagasin(Magasin Magasin) {
		// TODO Auto-generated method stub
		magasinRepository.findById(Magasin.getId());
	        return magasinRepository.save(Magasin);
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
