package com.example.stock.service;


import com.example.stock.dto.CategoryDto;
import com.example.stock.model.Category;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;



public interface CategoryService {

    Category ajouterCategory(Category ca);
    
    void updateCategorie(Integer id,Category Category);
    
    Category deleteCategory(Integer id);
    
    List<CategoryDto> findAll();
    public void generateEcel (HttpServletResponse response) throws IOException;
    
    Category retrieveCategory (Integer categoryId);
    
  //  public void saveCustomersToDatabase(MultipartFile file);
}