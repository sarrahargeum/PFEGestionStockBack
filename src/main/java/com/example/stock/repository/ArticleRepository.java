package com.example.stock.repository;

import com.example.stock.dto.ArticleDto;
import  com.example.stock.model.Article;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query("SELECT a FROM Article a WHERE a.code = :code")
    Article findByCodeArticle(String code);
    
    Optional<Article> findArticleByCode(String code);
    
    List<Article> findAllByCategoryId(Integer idCategory);
    
    @Query("SELECT new com.example.stock.dto.ArticleDto( " +
    	       "a.id, " +
    	       "a.code, " +
    	       "a.designation, " +
    	       "a.prix, " +
    	       "a.tauxTva, " +
    	       "a.image, " +
    	       "sum(s.quantite) as quantite) " +  
    	       "FROM Article a " +
    	       "LEFT JOIN MVTStock s ON a.id = s.article.id " +
    	       "GROUP BY a.id")
    	List<ArticleDto> findAllWithStocks();
}
