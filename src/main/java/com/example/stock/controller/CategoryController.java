package com.example.stock.controller;


import com.example.stock.model.Category;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.service.CategoryService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("/addCat")
    public Category ajouterCategory(@RequestBody Category ca){

        return categoryService.ajouterCategory(ca);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Category> findAll() {

        return categoryService.findAll();
    }

   
 
    
    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }

    
    @GetMapping("/retrieve-category/{id}")
    public Category retrieveCategory(@PathVariable("id") Integer categoryId) {
        return categoryService.retrieveCategory(categoryId);
    }
    
    @PutMapping("/Update/{id}")
	  public void updateCategorie(@PathVariable("id") Integer id, @RequestBody Category Category) {           
	       categoryService.updateCategorie(id, Category);
	   
	    
	  }

}
