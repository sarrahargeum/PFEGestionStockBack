package com.example.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stock.dto.BonEntreeDto;
import com.example.stock.model.BonEntree;
import com.example.stock.model.EtatCommande;
import com.example.stock.service.BonEntreeService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/BonEntree")
public class BonEntreeController {

	
	 @Autowired
	     BonEntreeService bonEntreeService;

	    @PostMapping("/saveBF")
	    public ResponseEntity<BonEntree> saveBonEntreFournisseur(@RequestBody BonEntree bonEntreFournisseur) {
	        BonEntree savedBonEntreFournisseur = bonEntreeService.save(bonEntreFournisseur);
	        return ResponseEntity.ok(savedBonEntreFournisseur);
	    }
	    
	    @GetMapping("/retreive-code/{code}")
	    public BonEntree findByCode(@PathVariable("code") String code){
	      return bonEntreeService.findByCode(code);
	    }
	    
	    @GetMapping( "/allBF")
	    public List<BonEntreeDto> findAll() {
	      return bonEntreeService.findAll();
	    }
	  
	    
	    @GetMapping("/retreive/{idbonEntrefournisseur}")
	    BonEntree findById(@PathVariable("idbonEntrefournisseur") Integer id) {
	    return bonEntreeService.findById(id);
	    }
	    
	    
	    @DeleteMapping("/delete/{id}")
	    void delete(@PathVariable("id") Integer id){
	    	bonEntreeService.delete(id);
	    }
	    
	    
	    @PutMapping("/update/etat/{id}/{etatCommande}")
	    BonEntree updateEtatCommande(@PathVariable("id") Integer id, @PathVariable("etatCommande") EtatCommande etatCommande) {
	    return bonEntreeService.updateEtatCommande(id, etatCommande);
	
	    }
	    
	    
	    @PutMapping("/quantite/{id}/{idligneEntreeFournisseur}/{quantite}")
	    BonEntree updateQuantiteCommande(@PathVariable("id") Integer id,
	    		@PathVariable("idligneEntreeFournisseur") Integer idligneEntreeFournisseur, @PathVariable("quantite") Integer quantite)
 {
	      return bonEntreeService.updateQuantiteCommande(id, idligneEntreeFournisseur,quantite);
	    }

	   
}
