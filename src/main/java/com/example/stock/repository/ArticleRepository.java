package com.example.stock.repository;

import com.example.stock.model.Article;
import com.example.stock.model.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    @Query("SELECT a FROM Article a WHERE a.code = :code")

        Article findByCodeArticle(String code);
  //  List<Article> findAll();
    Optional<Article> findArticleByCode(String code);

}
