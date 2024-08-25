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
import com.example.stock.dto.LigneEntreeDto;
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
	  BonEntreeDto save(@RequestBody BonEntreeDto bonEntreFournisseur) {
	    return bonEntreeService.save(bonEntreFournisseur);
	  }

	 

	    
	    @GetMapping("/retreive-code/{code}")
	    public BonEntreeDto findByCode(@PathVariable("code") String code){
	      return bonEntreeService.findByCode(code);
	    }
	    
	    @GetMapping( "/allBF")
	    public List<BonEntreeDto> findAll() {
	      return bonEntreeService.findAll();
	    }
	  
	    
	    @GetMapping("/retreive/{idbonEntrefournisseur}")
	    BonEntreeDto findById(@PathVariable("idbonEntrefournisseur") Integer id) {
	    return bonEntreeService.findById(id);
	    }
	    
	    
	    @DeleteMapping("/delete/{id}")
	    void delete(@PathVariable("id") Integer id){
	    	bonEntreeService.delete(id);
	    }
	    
	    
	    @PutMapping("/update/etat/{id}/{etatCommande}")
	    BonEntreeDto updateEtatCommande(@PathVariable("id") Integer id, @PathVariable("etatCommande") EtatCommande etatCommande) {
	    	
	    return bonEntreeService.updateEtatCommande(id, etatCommande);
	
	    }
	    
	    
	    @PutMapping("/quantite/{id}/{idligneEntreeFournisseur}/{quantite}")
	    BonEntreeDto updateQuantiteCommande(@PathVariable("id") Integer id,@PathVariable("idligneEntreeFournisseur") Integer idligneEntreeFournisseur, @PathVariable("quantite") Integer quantite){
	      return bonEntreeService.updateQuantiteCommande(id, idligneEntreeFournisseur,quantite);
	    }
	    
	    
	    @PutMapping("/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
	    BonEntreeDto updateArticle(@PathVariable("idCommande") Integer idCommande,@PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle) {
	        return bonEntreeService.updateArticle(idCommande, idLigneCommande, idArticle);
	      }
	    
	    @DeleteMapping("/delete/article/{idCommande}/{idLigneCommande}")
	    BonEntreeDto deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande){
	        return bonEntreeService.deleteArticle(idCommande, idLigneCommande);
	      }
	    
	    @GetMapping("/lignesCommande/{idCommande}")
	    List<LigneEntreeDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande) {
	        return bonEntreeService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande);
	      }
	    
	    @DeleteMapping("/deleteCmd/{id}")
	    BonEntreeDto deleteBonEntree(@PathVariable Integer id) {
	       
	           return bonEntreeService.deleteBonEntree(id);
	           
	        
	    }
	    
}
