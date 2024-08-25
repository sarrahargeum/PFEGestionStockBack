package com.example.stock.service.metiers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.dto.FournisseurDto;
import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.model.Article;
import com.example.stock.model.BonEntree;
import com.example.stock.model.Fournisseur;
import com.example.stock.repository.BonEntreeRepository;
import com.example.stock.repository.FournisseurRepository;
import com.example.stock.repository.LigneEntreeRepository;
import com.example.stock.service.FournisseurService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class FournisseurServiceImpl implements FournisseurService {
	
	@Autowired
	FournisseurRepository fournisseurRepository;
	@Autowired
	LigneEntreeRepository ligneEntreeRepository;
	@Autowired
	BonEntreeRepository bonEntreeRepository;
	public FournisseurDto ajouterFournisseur(FournisseurDto dto) {
	 
	    return FournisseurDto.fromEntity(
	        fournisseurRepository.save(
	            FournisseurDto.toEntity(dto)
	        )
	    );
	  }
	
	

	  @Override
	  public FournisseurDto retrieveFournisseur(Integer id) {
	    if (id == null) {
	      log.error("Fournisseur ID is null");
	      return null;
	    }
	    return fournisseurRepository.findById(id)
	        .map(FournisseurDto::fromEntity)
	        .orElseThrow(() -> new EntityNotFoundException(
	            "Aucun fournisseur avec l'ID = " + id + " n' ete trouve dans la BDD")
	        );
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
	
	


	@Transactional
	public Fournisseur deleteFournisseur(Integer id) {
		 Fournisseur fournisseur = fournisseurRepository.findById(id)
		            .orElseThrow(() -> new EntityNotFoundException("Fournisseur not found"));

		    // Manually delete related BonEntree records
		    for (BonEntree bonEntree : fournisseur.getBonEntrees()) {
		        // This will handle related LigneEntree records due to cascade settings
		        bonEntreeRepository.delete(bonEntree);
		    }

		    // Now delete the fournisseur
		    fournisseurRepository.delete(fournisseur);
		    
		    return fournisseur;
	}

	@Override
	public List<FournisseurDto> findAll() {
		return fournisseurRepository.findAll().stream()
		        .map(FournisseurDto::fromEntity)
		        .collect(Collectors.toList());
	}

	

}
