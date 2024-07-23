package com.example.stock.controller;


import com.example.stock.model.Category;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.service.CategoryService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    
    
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
    	response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=categorys.xls";
        response.setHeader(headerKey, headerValue);

        categoryService.generateEcel(response);
    }

    
   /* @PostMapping("/upload-customers-data")
    public ResponseEntity<?> uploadCustomersData(@RequestParam("file")MultipartFile file){
        this.categoryService.saveCustomersToDatabase(file);
        return ResponseEntity.ok(Map.of("Message" , "Customers data uploaded and saved to database successfully"));
    }*/

}
