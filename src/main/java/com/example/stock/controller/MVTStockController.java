package com.example.stock.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stock.dto.MVTStockDto;
import com.example.stock.model.MVTStock;
import com.example.stock.service.MVTStockService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/mvtstock")
public class MVTStockController {

	@Autowired
	MVTStockService mvtsService;
	
	  @GetMapping("/stockreel/{idArticle}")
	  
	  Integer stockReelArticle(@PathVariable("idArticle") Integer idArticle){
	 return mvtsService.stockReelArticle(idArticle);
	 
	}

	  
	   @GetMapping("/filter/article/{idArticle}")
	    public List<MVTStockDto> mvtStkArticle(@PathVariable("idArticle") Integer idArticle) {
	        return mvtsService.mvtStkArticle(idArticle);
	    }
	  
	  @PostMapping("/entree")
	 MVTStockDto entreeStock(@RequestBody MVTStockDto mvt){
	    return mvtsService.entreeStock(mvt);
	  }
	  
	  
	  @PostMapping("/sortie")
	 MVTStockDto sortieStock(@RequestBody MVTStockDto mvt){
	    return mvtsService.sortieStock(mvt);
	  }
	 

}
