package com.example.stock.controller;


import com.example.stock.model.Category;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.service.CategoryService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public void exportToExcel(HttpServletResponse response) {
    	System.out.println("Export to Excel....");
    	response.setContentType("application/octet-stream");
    	DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    	String currentDateTime = dateFormatter.format(new Date());
    	String headerKey = "Content-Disposition";
    	String heagerValue = "attachment; filename=categories_"+ currentDateTime + ".xlsx";
    	response.setHeader(headerKey, heagerValue);
    	List<Category> listCategories= categoryService.findAll();
    //	CategorieExcel excel = new CategorieExcel(ListCategorie)
       // excel.export(response);
    }

}
