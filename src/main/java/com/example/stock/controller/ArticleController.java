package com.example.stock.controller;

import com.example.stock.model.Article;

import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.CategoryRepository;
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

    
   /* @PostMapping("/add")
    public Article ajoutArticle(@RequestBody Article article) {
        Article articles= articleService.ajoutArticle(article.getImage(), article.getCode(), article.getDesignation(), article.getPrix(), article.getTauxTva(), article.getCategory().getId(), article.getMagasin().getId());
        return articles; // Returning the code as a response, adjust as needed
    }*/
    
    
    @PostMapping("/add")
    public ResponseEntity<Response> ajoutArticle(@RequestParam("file") MultipartFile file,
    		@RequestParam("article")String article
    		//@RequestParam("idcategory") Integer idcategory
    		) throws JsonParseException, JsonMappingException,Exception,IOException
    {
    	
    	System.out.println("Ok....");
   	
    	
    	Article arti = new ObjectMapper().readValue(article, Article.class)  ;
    	  // Fetch the category using the provided categoryId
       /* Category category = categoryRepository.findById(idcategory)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        arti.setCategory(category);*/
        
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
    	return new ResponseEntity<Response>(new Response (),HttpStatus.OK);
    }
    else {
    	return new ResponseEntity<Response>(new Response (),HttpStatus.BAD_REQUEST);

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


  /*  @GetMapping("/allArticle")
    public ResponseEntity<List<String>> getAll() {
        return articleService.getAll1();
    }*/
    
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
    
    @PutMapping("/update")
    public Article updateArticle(@RequestBody Article article) {
        return articleService.updateArticle(article);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
    }



    }
