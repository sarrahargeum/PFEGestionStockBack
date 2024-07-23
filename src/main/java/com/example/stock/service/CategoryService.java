package com.example.stock.service;


import com.example.stock.model.Category;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public interface CategoryService {

    Category ajouterCategory(Category ca);
    
    void updateCategorie(Integer id,Category Category);
    
    Category deleteCategory(Integer id);
    
    List<Category> findAll();
    public void generateEcel (HttpServletResponse response) throws IOException;
    
    Category retrieveCategory (Integer categoryId);
    
  //  public void saveCustomersToDatabase(MultipartFile file);
}