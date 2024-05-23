package com.example.stock.service.metiers;

import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.model.Article;
import com.example.stock.model.Category;
import com.example.stock.model.Magasin;
import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.repository.MagasinRepository;
import com.example.stock.service.ArticleService;
import com.google.common.io.Files;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl  implements ArticleService {
   
	@Autowired
    ArticleRepository articleRepository;
	
	@Autowired
    CategoryRepository categoryRepository;
	
	@Autowired
    MagasinRepository magasinRepository;
	
    @Autowired ServletContext context;

	
    private static  Log log = LogFactory.getLog(ArticleServiceImpl.class);

  /*  public void ajoutArticle(MultipartFile image, String code, String designation,
            float prix, float tauxTva, Integer categoryId, Integer magasinId) {
    	Article art = new Article();
    	String fileName = StringUtils.cleanPath(image.getOriginalFilename());

    	if (fileName.contains("..")) {
    		System.out.println("Not a valid file");
    		// Handle invalid file case here
    		return;
    	}

    	try {
    		art.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
    	} catch (IOException e) {
    		e.printStackTrace();
    		// Handle image processing error here
    		return;
    	}

    	art.setCode(code);
    	art.setDesignation(designation);
    	art.setPrix(prix);
    	art.setTauxTva(tauxTva);

    	// Fetch category and magasin objects from their respective repositories
    	Category category = categoryRepository.findById(categoryId).orElse(null);
    	Magasin magasin = magasinRepository.findById(magasinId).orElse(null);

if (category == null || magasin == null) {
System.out.println("Category or Magasin not found");

return;
}

art.setCategory(category);
art.setMagasin(magasin);

articleRepository.save(art);
}
*/

	

    @Override
    public Article findById(Integer id) {
        if (id == null) {
            log.error("Article ID is null");
            return null;
        }
        return articleRepository.findById(id)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "Aucun article avec l'ID = " + id + " n'ete trouve dans la BDD")
                );
    }

    @Override
    public Article findByCodeArticle(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Article CODE is null");
            return null;
        }

        return articleRepository.findByCodeArticle(code);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll().stream().collect(Collectors.toList());
    }
    
   
      



    public Article updateArticle(Article article) {
        articleRepository.findById(article.getId());
        return articleRepository.save(article);
    }

    public Article deleteArticle(Integer id) {
        articleRepository.deleteById(id);
        return null;
    }

	@Override
	public Article ajoutArticle(MultipartFile image) {
		// TODO Auto-generated method stub
		return null;
	}









}
