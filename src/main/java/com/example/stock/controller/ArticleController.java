package com.example.stock.controller;

import com.example.stock.dto.ArticleDto;
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
    public ResponseEntity<ArticleDto> ajoutArticle(@RequestParam("file") MultipartFile file,
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
	return null;
    
   /* if(art != null) {
    	return new ResponseEntity<>(art,HttpStatus.OK);
    }
    else {
    	return new ResponseEntity<>(art,HttpStatus.BAD_REQUEST);

    }*/
    }

    @GetMapping("/{idArticle}")
    public ArticleDto findById(@PathVariable("idArticle") Integer id) {
        return articleService.findById(id);
    }


   /* @GetMapping("/filter/{code}")
    public ArticleDto findByCodeArticle(@PathVariable String code) {
        return articleService.findByCodeArticle(code);
    }*/


    
    @GetMapping(path="/Imgarticles/{id}")
	 public byte[] getPhoto(@PathVariable("id") Integer id) throws Exception{
    	Article Article   = articleRepository.findById(id).get();
		 return Files.readAllBytes(Paths.get(context.getRealPath("/Images/")+Article.getImage()));
	 }
    
    
    
    @GetMapping("/allArticle")
    public List<ArticleDto> getAllArticle() {
        return articleService.getAllArticle();
    }
    
    

    @PutMapping("/update/{id}")
    public void update(@PathVariable Integer id, @RequestBody ArticleDto article) {
        articleService.update(id, article);
    }
    
  

    @GetMapping("/retrieve-article/{id}")
    public ArticleDto retrieveArticle(@PathVariable("id") Integer id) {
        return articleService.retrieveArticle(id);
    }
    
   /* @PutMapping("/update/{id}")
    public void update(@PathVariable Integer id,@RequestParam("file") MultipartFile file,
			 @RequestParam("article") String article) throws JsonParseException , JsonMappingException , Exception {
     Article art = new ObjectMapper().readValue(article, Article.class);
        	deleteArticleImage(art);
        	 String filename = file.getOriginalFilename();
     	    String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
     	    art.setImage(newFileName);
            articleService.update(id, art);
           
           ajoutArticle(file, article, id, id);
       
    }
    
    private void deleteArticleImage(Article article)
    {
    	System.out.println( " Delete Article Image");
         try { 
        	 File file = new File (context.getRealPath("/Images/"+article.getImage()));
             System.out.println(article.getImage());
              if(file.delete()) { 
                System.out.println(file.getName() + " is deleted!");
             } else {
                System.out.println("Delete operation is failed.");
                }
          }
            catch(Exception e)
            {
                System.out.println("Failed to Delete image !!");
            }
    }*/
    
    
    @DeleteMapping("/delete/{id}")
    public void deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
    }



    }
