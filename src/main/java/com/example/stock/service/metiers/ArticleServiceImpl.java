package com.example.stock.service.metiers;

import com.example.stock.dto.ArticleDto;
import com.example.stock.dto.LigneEntreeDto;
import com.example.stock.dto.LigneSortieDto;
import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.model.Article;

import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.repository.LigneEntreeRepository;
import com.example.stock.repository.LigneSortieRepository;
import com.example.stock.repository.MagasinRepository;
import com.example.stock.service.ArticleService;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
	
	@Autowired
	LigneEntreeRepository ligneEntreeRepository;
	
	@Autowired
	LigneSortieRepository ligneSortieRepository;
	
    @Autowired ServletContext context;

	
    private static  Log log = LogFactory.getLog(ArticleServiceImpl.class);


    @Override
    public Article findById(Integer id) {
        if (id == null) {
            log.error("Article ID is null");
            return null;
        }
        return articleRepository.findById(id).orElseThrow(() ->
        new EntityNotFoundException(
            "Aucun article avec l'ID = " + id + " n' ete trouve dans la BDD")
    );
    }

   /* @Override
    public ArticleDto findByCodeArticle(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Article CODE is null");
            return null;
        }
        return articleRepository.findByCodeArticle(code).map(ArticleDto::fromEntity)
                .orElseThrow(() ->
                    new EntityNotFoundException(
                        "Aucun article avec le CODE = " + codeArticle + " n' ete trouve dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
                );

    }*/

    @Override
    public List<Article> getAllArticle() {
    	 return articleRepository.findAll().stream()
    		        .collect(Collectors.toList());
    }
    
   
    

      

    public Article retrieveArticle (Integer id){ 
    	if (id == null) {
        log.error("Article ID is null");
        return null;
    }
    return articleRepository.findById(id).orElseThrow(() ->
    new EntityNotFoundException(
        "Aucun article avec l'ID = " + id + " n' ete trouve dans la BDD")
);
    }



    
	
    
    public void update(Integer id, Article Article) {
        Optional<Article> arti = articleRepository.findById(id);
        if (arti.isPresent()) {
        	Article article = arti.get();
        	article.setCode(Article.getCode());
	        article.setDesignation(Article.getDesignation());
	        article.setPrix(Article.getPrix());
	        article.setTauxTva(Article.getTauxTva());
	       // article.setCategory(Article.getCategory());
	       
	        Article art = articleRepository.save(article);
        }
		
    }
    
    

    public Article deleteArticle(Integer id) {
        articleRepository.deleteById(id);
        return null;
    }


    @Override
    public List<LigneSortieDto> findHistoriaueCommandeClient(Integer idArticle) {
      return ligneSortieRepository.findAllByArticleId(idArticle).stream()
          .map(LigneSortieDto::fromEntity)
          .collect(Collectors.toList());
    }

    @Override
    public List<LigneEntreeDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
      return ligneEntreeRepository.findAllByArticleId(idArticle).stream()
          .map(LigneEntreeDto::fromEntity)
          .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory) {
      return articleRepository.findAllByCategoryId(idCategory).stream()
          .map(ArticleDto::fromEntity)
          .collect(Collectors.toList());
    }







}
