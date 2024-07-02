package com.example.stock.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.stock.model.MVTStock;

public interface MVTStockService {

	
	  Integer stockReelArticle(Integer idArticle);

	  List<MVTStock> mvtStkArticle(Integer idArticle);
	  MVTStock entreeStock(MVTStock mvt);
	  MVTStock sortieStock(MVTStock dto);

}
