package com.example.stock.controller;


import com.example.stock.model.Article;
import com.example.stock.model.Category;
import com.example.stock.model.User;
import com.example.stock.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/addCat")
    public Category ajouterCategory(@RequestBody Category ca){

        return categoryService.ajouterCategory(ca);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Category> findAll() {

        return categoryService.findAll();
    }

    @PutMapping("/update")
    public Category updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }
    
    
    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }

    
    @GetMapping("/retrieve-category/{id}")
    public Category retrieveCategory(@PathVariable("id") Integer categoryId) {
        return categoryService.retrieveCategory(categoryId);
    }

}
