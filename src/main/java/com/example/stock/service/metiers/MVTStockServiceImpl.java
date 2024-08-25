package com.example.stock.service.metiers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.dto.MVTStockDto;
import com.example.stock.exception.InvalidEntityException;

import com.example.stock.model.TypeStock;
import com.example.stock.model.User;
import com.example.stock.repository.MVTStockRepository;
import com.example.stock.service.ArticleService;
import com.example.stock.service.MVTStockService;
import com.example.stock.validator.StockValidator;



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
	public List<MVTStockDto> mvtStkArticle(Integer idArticle) {

	    return mvtrepository.findAllByArticleId(idArticle);
	}

	

	
	  
	  
	  private MVTStockDto entreePositive(MVTStockDto mvt, TypeStock typeMvtStk) {
		    List<String> errors = StockValidator.validate(mvt);
		    if (!errors.isEmpty()) {
		      log.error("Article is not valid {}", mvt);
		      throw new InvalidEntityException("Le mouvement du stock n'est pas valide");
		    }
		    mvt.setQuantite(
		    		 Math.abs(mvt.getQuantite())
		    );
		    mvt.setTypeMvt(typeMvtStk);
		    return  MVTStockDto.fromEntity(
		        mvtrepository.save(MVTStockDto.toEntity(mvt)));
		  }
	  
	  private MVTStockDto sortieNegative(MVTStockDto dto, TypeStock typeMvtStk) {
		    List<String> errors = StockValidator.validate(dto);
		    if (!errors.isEmpty()) {
		      log.error("Article is not valid {}", dto);
		      throw new InvalidEntityException("Le mouvement du stock n'est pas valide", errors);
		    }
		    dto.setQuantite(

		            dto.getQuantite() * -1
		        
		    );
		    dto.setTypeMvt(typeMvtStk);
		    return  MVTStockDto.fromEntity(
			        mvtrepository.save(MVTStockDto.toEntity(dto)));
		    
		  }

	@Override
	public MVTStockDto entreeStock(MVTStockDto mvt) {
		 return entreePositive(mvt, TypeStock.ENTREE);
	}

	@Override
	public MVTStockDto sortieStock(MVTStockDto dto) {
		 return sortieNegative(dto, TypeStock.SORTIE);
	}
	  
}
