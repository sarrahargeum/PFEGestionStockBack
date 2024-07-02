package com.example.stock.service.metiers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.exception.InvalidEntityException;
import com.example.stock.model.Category;
import com.example.stock.model.Fournisseur;
import com.example.stock.model.MVTStock;
import com.example.stock.model.TypeStock;
import com.example.stock.model.User;
import com.example.stock.repository.MVTStockRepository;
import com.example.stock.service.ArticleService;
import com.example.stock.service.MVTStockService;
import com.example.stock.validator.StockValidator;

import ch.qos.logback.core.spi.ErrorCodes;


@Service
public class MVTStockServiceImpl implements MVTStockService {

    private final Logger log = LoggerFactory.getLogger(User.class);

	@Autowired
	 MVTStockRepository mvtrepository;
	@Autowired
	ArticleService articleService;
	
	@Override
	public Integer stockReelArticle(Integer idArticle) {
		   if (idArticle == null) {
			      log.error("ID article is NULL");
			      return -1;
			    }
			    articleService.findById(idArticle);
			    return mvtrepository.stockReelArticle(idArticle);
	}

	@Override
	public List<MVTStock> mvtStkArticle(Integer idArticle) {

	    return mvtrepository.findAllByArticleId(idArticle);
	}

	

	  @Override
	  public MVTStock entreeStock(MVTStock mvt) {
	    return entreePositive(mvt, TypeStock.ENTREE);
	  }
	  
	  
	  private MVTStock entreePositive(MVTStock mvt, TypeStock typeMvtStk) {
		    List<String> errors = StockValidator.validate(mvt);
		    if (!errors.isEmpty()) {
		      log.error("Article is not valid {}", mvt);
		      throw new InvalidEntityException("Le mouvement du stock n'est pas valide");
		    }
		    mvt.setQuantite(
		    		 Math.abs(mvt.getQuantite())
		    );
		    mvt.setTypestock(typeMvtStk);
		    return 
		        mvtrepository.save(mvt);
		  }
	  
	  private MVTStock sortieNegative(MVTStock mvt, TypeStock typeMvtStk) {
		    List<String> errors = StockValidator.validate(mvt);
		    if (!errors.isEmpty()) {
		      log.error("Article is not valid {}", mvt);
		      throw new InvalidEntityException("Le mouvement du stock n'est pas valide", errors);
		    }
		    mvt.setQuantite(

		            mvt.getQuantite() * -1
		        
		    );
		    mvt.setTypestock(typeMvtStk);
		    return 
		        mvtrepository.save(mvt);
		    
		  }

	  @Override
	  public MVTStock sortieStock(MVTStock dto) {
	    return sortieNegative(dto, TypeStock.SORTIE);
	  }
	  
}
