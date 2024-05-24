package com.example.stock.service.metiers;

import com.example.stock.model.Article;
import com.example.stock.model.Category;
import com.example.stock.model.User;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiveImpl implements CategoryService {


    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category ajouterCategory(Category ca) {
        return categoryRepository.save(ca);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll().stream().collect(Collectors.toList());
    }


    public Category deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
        return null;
    }
    
    
    public Category retrieveCategory (Integer categoryId){
        Category cat = categoryRepository.findById(categoryId).get();
        return  cat;
    }

    
	  public void updateCategorie(Integer id, Category Category) {
		    System.out.println("Update Categorie with ID = " + id + "...");
		 
		    Optional<Category> categoryInfo = categoryRepository.findById(id);
		 
		    if (categoryInfo.isPresent()) {
		    	Category categorie = categoryInfo.get();
		    	categorie.setCode(Category.getCode());
		          categorie.setDesignation(Category.getDesignation());
		           
		          Category cat = categoryRepository.save(categorie);
		         
		  }
	  }
}