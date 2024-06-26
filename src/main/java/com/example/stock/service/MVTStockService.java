package com.example.stock.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.stock.model.MVTStock;

public interface MVTStockService {

	
	  BigDecimal stockReelArticle(Integer idArticle);

	  List<MVTStock> mvtStkArticle(Integer idArticle);
	  public MVTStock entreeStock(MVTStock mvt);
}
