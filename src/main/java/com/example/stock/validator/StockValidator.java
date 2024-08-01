package com.example.stock.validator;


import com.example.stock.dto.MVTStockDto;
import com.example.stock.model.MVTStock;

import lombok.Data;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class StockValidator {

  public static List<String> validate(MVTStockDto stock) {
    List<String> errors = new ArrayList<>();
    if (stock == null) {
      errors.add("Veuillez renseigner la date du mouvenent");
      errors.add("Veuillez renseigner la quantite du mouvenent");
      errors.add("Veuillez renseigner l'article");
      errors.add("Veuillez renseigner le type du mouvement");

      return errors;
    }
    if (stock.getDateMvt() == null) {
      errors.add("Veuillez renseigner la date du mouvenent");
    }
    if (stock.getQuantite() == null || stock.getQuantite() == 0) {
      errors.add("Veuillez renseigner la quantite du mouvenent");
    }
    if (stock.getArticle() == null || stock.getArticle().getId() == null) {
      errors.add("Veuillez renseigner l'article");
    }
    if (!StringUtils.hasLength(stock.getTypeMvt().name())) {
      errors.add("Veuillez renseigner le type du mouvement");
    }

    return errors;
  }

}
