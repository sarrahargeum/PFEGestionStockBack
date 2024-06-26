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

import com.example.stock.model.BonEntreFournisseur;
import com.example.stock.model.EtatCommande;
import com.example.stock.repository.BonEntreFournisseurRepository;
import com.example.stock.service.BonEntreFournisseurService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/BonEntreFournisseur")
public class BonEntreFournisseurController {
	
	
	
	@Autowired
	BonEntreFournisseurRepository bonEntreRepository;
	
	
	
	 @Autowired
	    private BonEntreFournisseurService bonEntreFournisseurService;

	    @PostMapping("/saveBF")
	    public ResponseEntity<BonEntreFournisseur> saveBonEntreFournisseur(@RequestBody BonEntreFournisseur bonEntreFournisseur) {
	        BonEntreFournisseur savedBonEntreFournisseur = bonEntreFournisseurService.save(bonEntreFournisseur);
	        return ResponseEntity.ok(savedBonEntreFournisseur);
	    }
	    
	    @GetMapping("/retreive-code/{code}")
	    public BonEntreFournisseur findByCode(@PathVariable("code") String code){
	      return bonEntreFournisseurService.findByCode(code);
	    }
	    
	    @GetMapping( "/allBF")
	    public List<BonEntreFournisseur> findAll() {
	      return bonEntreFournisseurService.findAll();
	    }
	    
	    @GetMapping("/retreive/{idbonEntrefournisseur}")
	    BonEntreFournisseur findById(@PathVariable("idbonEntrefournisseur") Integer id) {
	    return bonEntreFournisseurService.findById(id);
	    }
	    
	    
	    @DeleteMapping("/delete/{id}")
	    void delete(@PathVariable("id") Integer id){
	    	bonEntreFournisseurService.delete(id);
	    }
	    
	    
	    @PutMapping("/update/etat/{id}/{etatCommande}")
	    BonEntreFournisseur updateEtatCommande(@PathVariable("id") Integer id, @PathVariable("etatCommande") EtatCommande etatCommande) {
	    return bonEntreFournisseurService.updateEtatCommande(id, etatCommande);
	
	    }
}
