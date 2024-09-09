package com.example.stock.controller;

import java.util.List;
import java.util.Optional;

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
import com.example.stock.dto.BonSortieDto;
import com.example.stock.dto.ClientDto;
import com.example.stock.dto.LigneSortieDto;
import com.example.stock.model.BonSortie;
import com.example.stock.model.Client;
import com.example.stock.model.EtatCommande;
import com.example.stock.repository.ClientRepository;
import com.example.stock.service.BonSortieService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/BonSortie")
public class BonSortieController {

	 @Autowired
	     BonSortieService bonSortieService;
	 
	 @Autowired
	 ClientRepository clientRepository;
	    @PostMapping("/saveBS")
	    BonSortieDto save(@RequestBody BonSortieDto bonSortie) {
		    return bonSortieService.save(bonSortie);

	    
	    }
	    
	    
	    @PostMapping("/saveBSClient")
	    BonSortieDto saveBSClient(@RequestBody BonSortieDto bonSortie) {
	    	// Validation et enregistrement du client
	        Optional<Client> client = clientRepository.findById(bonSortie.getClient().getId());
	        if (client.isEmpty()) {
	            ClientDto clientDto = bonSortie.getClient();
	            Client newClient = ClientDto.toEntity(clientDto);
	            clientRepository.save(newClient);
	        }

	        // Validation et enregistrement du bon de sortie
	     //   BonSortieDto savedBonSortie = bonSortieService.saveBSClient(bonSortie);

		    return bonSortieService.save(bonSortie);
	    
	    }
	    @GetMapping("/retreive-code/{code}")
	    public BonSortieDto findByCode(@PathVariable("code") String code){
	      return bonSortieService.findByCode(code);
	    }
	    
	    @GetMapping( "/allBS")
	    public List<BonSortieDto> findAll() {
	      return bonSortieService.findAll();
	    }
	    
	    @GetMapping("/retreive/{idbonSortie}")
	    BonSortieDto findById(@PathVariable("idbonSortie") Integer id) {
	    return bonSortieService.findById(id);
	    }
	    
	    
	    @DeleteMapping("/delete/{id}")
	    void delete(@PathVariable("id") Integer id){
	    	bonSortieService.delete(id);
	    }
	    
	    
	    @PutMapping("/update/etat/{id}/{etatCommande}")
	    BonSortieDto updateEtatCommande(@PathVariable("id") Integer id, @PathVariable("etatCommande") EtatCommande etatCommande) {
	    return bonSortieService.updateEtatCommande(id, etatCommande);
	
	    }
	    
	    
	    @PutMapping("/quantite/{id}/{idligneSortie}/{quantite}")
	    BonSortieDto updateQuantiteCommande(@PathVariable("id") Integer id,
	    		@PathVariable("idligneSortie") Integer idligneSortie, @PathVariable("quantite") Integer quantite){
	      return bonSortieService.updateQuantiteCommande(id, idligneSortie,quantite);
	    }

	    @PutMapping("/commandesclients/update/client/{idCommande}/{idClient}")
	    ResponseEntity<BonSortieDto> updateClient(@PathVariable("idCommande") Integer idCommande, @PathVariable("idClient") Integer idClient){
	      return ResponseEntity.ok(bonSortieService.updateClient(idCommande, idClient));
	    }

	    @PutMapping( "/commandesclients/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
	    ResponseEntity<BonSortieDto> updateArticle(@PathVariable("idCommande") Integer idCommande,
	        @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle) {
	        return ResponseEntity.ok(bonSortieService.updateArticle(idCommande, idLigneCommande, idArticle));
	      }

	    @GetMapping("/commandesclients/lignesCommande/{idCommande}")
	    ResponseEntity<List<LigneSortieDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande) {
	        return ResponseEntity.ok(bonSortieService.findAllLignesCommandesClientByCommandeClientId(idCommande));
	      }
	    
	    @DeleteMapping("/commandesclients/delete/article/{idCommande}/{idLigneCommande}")
	    ResponseEntity<BonSortieDto> deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande){
	        return ResponseEntity.ok(bonSortieService.deleteArticle(idCommande, idLigneCommande));
	      }
	    
	    @DeleteMapping("/deleteCmd/{id}")
	   BonSortieDto deleteBonSortie(@PathVariable Integer id) {
	           return bonSortieService.deleteBonSortie(id);
	       }
	    
	    @GetMapping("/count")
	    public ResponseEntity<Long> countBonSorties() {
	        long bonSortieCount = bonSortieService.countBonSorties();
	        return ResponseEntity.ok(bonSortieCount);
	    }
	    
}
