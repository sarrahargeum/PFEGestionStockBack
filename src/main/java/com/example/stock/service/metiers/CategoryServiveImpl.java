package com.example.stock.service.metiers;

import com.example.stock.model.Article;
import com.example.stock.model.Category;
import com.example.stock.model.User;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Category updateCategory(Category  category) {
        categoryRepository.findById(category.getId());
        return categoryRepository.save(category);
    }

    public Category deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
        return null;
    }
    
    
    public Category retrieveCategory (Integer categoryId){
        Category cat = categoryRepository.findById(categoryId).get();
        return  cat;
    }

}