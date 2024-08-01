package com.example.stock.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.stock.dto.MVTStockDto;
import com.example.stock.model.MVTStock;

public interface MVTStockService {

	
	  Integer stockReelArticle(Integer idArticle);

	  List<MVTStockDto> mvtStkArticle(Integer idArticle);
	  MVTStockDto entreeStock(MVTStockDto mvt);
	  MVTStockDto sortieStock(MVTStockDto dto);

}
