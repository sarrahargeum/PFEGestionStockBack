package com.example.stock.validator;

import com.example.stock.model.Article;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ArticleValidator {

  public static List<String> validate(Article article) {
    List<String> errors = new ArrayList<>();

    if (article == null) {
      errors.add("Veuillez renseigner le code de l'article");
      errors.add("Veuillez renseigner la designation de l'article");
      errors.add("Veuillez renseigner le prix unitaire HT l'article");
      errors.add("Veuillez renseigner le taux TVA de l'article");

      errors.add("Veuillez selectionner une categorie");
      return errors;
    }

    if (!StringUtils.hasLength(article.getCode())) {
      errors.add("Veuillez renseigner le code de l'article");
    }
    if (!StringUtils.hasLength(article.getDesignation())) {
      errors.add("Veuillez renseigner la designation de l'article");
    }
    if (article.getPrix() == null) {
      errors.add("Veuillez renseigner le prix unitaire HT l'article");
    }
    if (article.getTauxTva()== null) {
      errors.add("Veuillez renseigner le taux TVA de l'article");
    }

    if (article.getCategory() == null || article.getCategory().getId() == null) {
      errors.add("Veuillez selectionner une categorie");
    }
    return errors;
  }

}
