package com.example.stock.service;


import com.example.stock.model.Category;
import java.util.List;


public interface CategoryService {

    Category ajouterCategory(Category ca);
    
    void updateCategorie(Integer id,Category Category);
    
    Category deleteCategory(Integer id);
    
    List<Category> findAll();
    
    
    Category retrieveCategory (Integer categoryId);
}