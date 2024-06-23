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
import com.example.stock.model.Magasin;
import com.example.stock.service.MagasinService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/magasin")
public class MagasinController {

	
	 @Autowired
	    MagasinService magasinService;

	    @PostMapping("/addMg")
	    public Magasin ajouMagasin(@RequestBody Magasin mg){

	        return magasinService.ajouterMagasin(mg);
	    }

	    @GetMapping("/all")
	    @ResponseBody
	    public List<Magasin> findAll() {

	        return magasinService.findAll();
	    }

	    @PutMapping("/update/{id}")
	    public void updateMagasin(@PathVariable("id") Integer id,@RequestBody Magasin magasin) {
	    	magasinService.updateMagasin(id, magasin);
	    }
	    
	    @GetMapping("/retrieve-magasin/{id}")
	    public Magasin retrieveMagasin(@PathVariable("id") Integer magasinId) {
	        return magasinService.retrieveMagasin(magasinId);
	    }
	    
	    @DeleteMapping("/delete/{id}")
	    public void deleteMagasin(@PathVariable Integer id) {
	        magasinService.deleteMagasin(id);
	    }
}
