package com.example.stock.controller;

import com.example.stock.model.Article;
import com.example.stock.model.Category;
import com.example.stock.model.Magasin;
import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.repository.MagasinRepository;
import com.example.stock.service.ArticleService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletContext;

import org.apache.catalina.connector.Response;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/article")
public class ArticleController {
    @Autowired
     ArticleService articleService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired ServletContext context;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MagasinRepository magasinRepository;
 
    
    
    @PostMapping("/add")
    public ResponseEntity<Article> ajoutArticle(@RequestParam("file") MultipartFile file,
    		@RequestParam("article")String article,
    		@RequestParam("categoryId") Integer categoryId,
    		@RequestParam("magasinId") Integer magasinId
    		) throws JsonParseException, JsonMappingException,Exception,IOException
    {
    	
    	System.out.println("Ok....");
   	
    	
    	Article arti = new ObjectMapper().readValue(article, Article.class)  ;
    	  // Fetch the category using the provided categoryId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        arti.setCategory(category);
        
        Magasin magasin = magasinRepository.findById(magasinId)
                .orElseThrow(() -> new RuntimeException("magasin not found"));

        arti.setMagasin(magasin);
        
        
    	boolean isExit = new File(context.getRealPath("/Images/")).exists();
    
    if(!isExit)
    {
    	new File (context.getRealPath("/Images/")).mkdir();
    	System.out.println("mk dir....");
    	 
    }
    String filename = file.getOriginalFilename();
    String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
    File serverFile = new File (context.getRealPath("/Images/"+File.separator+newFileName));
    try {
    	System.out.println("Image");
    	FileUtils.writeByteArrayToFile(serverFile,file.getBytes());
    }catch(Exception e ) {
    	e.printStackTrace();
    }
    
    arti.setImage(newFileName);    
    Article art = articleRepository.save(arti);
    
    if(art != null) {
    	return new ResponseEntity<>(art,HttpStatus.OK);
    }
    else {
    	return new ResponseEntity<>(art,HttpStatus.BAD_REQUEST);

    }
    }

    @GetMapping("/{idArticle}")
    public Article findById(@PathVariable("idArticle") Integer id) {
        return articleService.findById(id);
    }


    @GetMapping("/filter/{code}")
    public Article findByCodeArticle(@PathVariable String code) {
        return articleService.findByCodeArticle(code);
    }


    
    @GetMapping(path="/Imgarticles/{id}")
	 public byte[] getPhoto(@PathVariable("id") Integer id) throws Exception{
		 Article Article   = articleRepository.findById(id).get();
		 return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+Article.getImage()));
	 }
    
    
    
    @GetMapping("/all")
    @ResponseBody
    public List<Article> findAll() {
        return articleService.findAll();
    }
    
   /* @PutMapping("/update")
    public Article updateArticle(@RequestBody Article article) {
        return articleService.updateArticle(article);
    }*/
    @PutMapping("/update/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") Integer id, @RequestBody Article Article) {
    	
    	return articleService.updateArticle(id, Article);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
    }



    }
