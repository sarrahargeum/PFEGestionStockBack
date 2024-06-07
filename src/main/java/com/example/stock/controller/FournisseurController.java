package com.example.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.stock.model.Fournisseur;

import com.example.stock.service.FournisseurService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/fournisseur")
public class FournisseurController {
	
	 @Autowired
	    FournisseurService fournisseurService;

	    @PostMapping("/addFour")
	    public  Fournisseur ajouterFournisseur(@RequestBody Fournisseur four){
	        return fournisseurService.ajouterFournisseur(four);
	        }

	    @GetMapping("/all")
	    @ResponseBody
	    public List<Fournisseur> findAll() {

	        return fournisseurService.findAll();
	    }

	    @PutMapping("/update")
	    public Fournisseur updateFournisseur(@RequestBody Fournisseur fournisseur) {
	        return fournisseurService.updateFournisseur(fournisseur);
	    }
	    @DeleteMapping("/delete/{id}")
	    public void deleteFournisseur(@PathVariable Integer id) {
	        fournisseurService.deleteFournisseur(id);
	    }
}
