package com.example.stock.service;


import com.example.stock.model.Article;
import com.example.stock.model.Category;
import com.example.stock.model.User;

import java.util.List;

public interface CategoryService {

    Category ajouterCategory(Category ca);
    Category updateCategory(Category category);
    Category deleteCategory(Integer id);
    List<Category> findAll();
    
    
    Category retrieveCategory (Integer categoryId);
}