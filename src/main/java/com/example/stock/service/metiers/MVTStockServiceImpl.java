package com.example.stock.service.metiers;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.controller.NotificationController;
import com.example.stock.dto.MVTStockDto;
import com.example.stock.exception.InvalidEntityException;
import com.example.stock.model.Article;
import com.example.stock.model.MVTStock;
import com.example.stock.model.Notification;
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
	
	@Autowired
	NotificationController notificationController;
	@Autowired
    NotificationServiceImpl notificationService;
	
    private static final int STOCK_MINIMUM = 9;

    public Integer stockReelArticle(Integer idArticle) {
        if (idArticle == null) {
            log.error("ID article is NULL");
            return -1;
        }
        Integer stockReel = mvtrepository.stockReelArticle(idArticle);

        if (stockReel != null && stockReel <= STOCK_MINIMUM) {		    	    
            String notificationMessage = "Attention: le stock de article  " + idArticle + " est  " + stockReel + " unites.";
            notificationController.sendOutOfStockNotification(notificationMessage);

            // Créer et enregistrer la notification dans la base de données
            Notification notification = new Notification();
            notification.setMessage(notificationMessage);
            notification.setDateNotification(Instant.now());
            notification.setEtatNotification(false);  
            notification.setType("OutOfStock");

            // Si article existe, il faut obtenir son code ou l'ajouter comme paramètre
            String codeCommande = getCodeCommandeForArticle(idArticle); 
            notification.setCodeCommande(codeCommande);

            notificationService.save(notification);
        }

        return stockReel;
    }

    // Méthode auxiliaire pour obtenir le code commande (si nécessaire)
    private String getCodeCommandeForArticle(Integer idArticle) {
        Article article = articleService.findById(idArticle);
        return article != null ? article.getCode() : "Unknown";
    }
	@Override
	public List<MVTStockDto> mvtStkArticle(Integer idArticle) {
	    List<MVTStock> mvtStocks = mvtrepository.findAllByArticleId(idArticle);

	    return mvtStocks.stream()
	        .map(MVTStockDto::fromEntity)
	        .collect(Collectors.toList());
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
		    dto.setQuantite(dto.getQuantite() * -1);
		    dto.setTypeMvt(typeMvtStk);
		    return MVTStockDto.fromEntity(mvtrepository.save(MVTStockDto.toEntity(dto)));
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
