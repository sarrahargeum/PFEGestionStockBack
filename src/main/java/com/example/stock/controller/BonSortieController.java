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
import com.example.stock.model.BonSortie;
import com.example.stock.model.EtatCommande;
import com.example.stock.service.BonSortieService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/BonSortie")
public class BonSortieController {

	 @Autowired
	     BonSortieService bonSortieService;

	    @PostMapping("/saveBS")
	    public ResponseEntity<BonSortie> saveBonSortie(@RequestBody BonSortie bonSortie) {
	    	BonSortie savedBonSortie = bonSortieService.save(bonSortie);
	        return ResponseEntity.ok(savedBonSortie);
	    }
	    
	    @GetMapping("/retreive-code/{code}")
	    public BonSortie findByCode(@PathVariable("code") String code){
	      return bonSortieService.findByCode(code);
	    }
	    
	    @GetMapping( "/allBS")
	    public List<BonSortie> findAll() {
	      return bonSortieService.findAll();
	    }
	    
	    @GetMapping("/retreive/{idbonSortie}")
	    BonSortie findById(@PathVariable("idbonEntrefournisseur") Integer id) {
	    return bonSortieService.findById(id);
	    }
	    
	    
	    @DeleteMapping("/delete/{id}")
	    void delete(@PathVariable("id") Integer id){
	    	bonSortieService.delete(id);
	    }
	    
	    
	    @PutMapping("/update/etat/{id}/{etatCommande}")
	    BonSortie updateEtatCommande(@PathVariable("id") Integer id, @PathVariable("etatCommande") EtatCommande etatCommande) {
	    return bonSortieService.updateEtatCommande(id, etatCommande);
	
	    }
	    
	    
	    @PutMapping("/quantite/{id}/{idligneSortie}/{quantite}")
	    BonSortie updateQuantiteCommande(@PathVariable("id") Integer id,
	    		@PathVariable("idligneEntreeFournisseur") Integer idligneSortie, @PathVariable("quantite") Integer quantite)
{
	      return bonSortieService.updateQuantiteCommande(id, idligneSortie,quantite);
	    }

	   



}
