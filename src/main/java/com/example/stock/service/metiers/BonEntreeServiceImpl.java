package com.example.stock.service.metiers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.controller.NotificationController;
import com.example.stock.dto.ArticleDto;
import com.example.stock.dto.BonEntreeDto;
import com.example.stock.dto.FournisseurDto;
import com.example.stock.dto.LigneEntreeDto;
import com.example.stock.dto.MVTStockDto;
import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.exception.InvalidEntityException;
import com.example.stock.exception.InvalidOperationException;
import com.example.stock.model.Article;
import com.example.stock.model.BonEntree;
import com.example.stock.model.EtatCommande;
import com.example.stock.model.Fournisseur;
import com.example.stock.model.LigneEntree;
import com.example.stock.model.TypeStock;
import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.BonEntreeRepository;
import com.example.stock.repository.FournisseurRepository;
import com.example.stock.repository.LigneEntreeRepository;
import com.example.stock.service.BonEntreeService;
import com.example.stock.service.MVTStockService;
import com.example.stock.validator.ArticleValidator;
import com.example.stock.validator.BonEntreeFournisseurValidator;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BonEntreeServiceImpl implements BonEntreeService{
	
	
	@Autowired
	BonEntreeRepository bonEntreeRepository;

	@Autowired
	FournisseurRepository fournisseurRepository;
	
	@Autowired
	ArticleRepository articleRepository;

	
	@Autowired
	LigneEntreeRepository ligneEntreeFournisseurRepository;
	
	@Autowired
	MVTStockService mvtStockService;

	@Autowired
	NotificationController notificationController;

	
	 public BonEntreeDto save(BonEntreeDto BEntree) {

		 List<String> errors = BonEntreeFournisseurValidator.validate(BEntree);

		    if (!errors.isEmpty()) {
		      log.error("Commande fournisseur n'est pas valide");
		      throw new InvalidEntityException("La commande fournisseur n'est pas valide");
		    }

		    if (BEntree.getId() != null && BEntree.isCommandeLivree()) {
		      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree");
		    }

		    Optional<Fournisseur> fournisseur = fournisseurRepository.findById(BEntree.getFournisseur().getId());
		    if (fournisseur.isEmpty()) {
		      log.warn("Fournisseur with ID {} was not found in the DB", BEntree.getFournisseur().getId());
		      throw new EntityNotFoundException("Aucun fournisseur avec l'ID" + BEntree.getFournisseur().getId() + " n'a ete trouve dans la BDD");
		    }

		    List<String> articleErrors = new ArrayList<>();

		    if (BEntree.getLigneEntrees() != null) {
		    	BEntree.getLigneEntrees().forEach(ligCmdFrs -> {
		        if (ligCmdFrs.getArticle() != null) {
		          Optional<Article> article = articleRepository.findById(ligCmdFrs.getArticle().getId());
		          if (article.isEmpty()) {
		            articleErrors.add("L'article avec l'ID " + ligCmdFrs.getArticle().getId() + " n'existe pas");
		          }
		        } else {
		          articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
		        }
		      });
		    }

		    if (!articleErrors.isEmpty()) {
		      log.warn("");
		      throw new InvalidEntityException("Article n'existe pas dans la BDD");
		    }
		    BEntree.setDateCommande(Instant.now());
		    
		    BonEntree savedCmdFrs = bonEntreeRepository.save(BonEntreeDto.toEntity(BEntree));

		    if (BEntree.getLigneEntrees() != null) {
		    	BEntree.getLigneEntrees().forEach(ligCmdFrs -> {
		        LigneEntree ligneEntree = LigneEntreeDto.toEntity(ligCmdFrs);
		        ligneEntree.setBonEntree(savedCmdFrs);
		        ligneEntree.setIdMagasin(savedCmdFrs.getIdMagasin());
		        LigneEntree saveLigne = ligneEntreeFournisseurRepository.save(ligneEntree);

		        effectuerEntree(saveLigne);
		      });
		    }
		      // Send WebSocket notification
	      //  notificationController.sendNotification("Commande " + savedCmdFrs.getId() + " saved. Please update status.");
	        return BonEntreeDto.fromEntity(savedCmdFrs);
		 

	}
	  private void effectuerEntree(LigneEntree lig) {
		    MVTStockDto mvtStkDto = MVTStockDto.builder()
		        .article(ArticleDto.fromEntity(lig.getArticle()))
		        .dateMvt(Instant.now())
		        .typeMvt(TypeStock.ENTREE)
		        .quantite(lig.getQuantite())
		        .idMagasin(lig.getIdMagasin())
		        .build();
		    mvtStockService.entreeStock(mvtStkDto);
		  }
		


	   

	   /* @Override
	    public BonEntreeDto save(BonEntreeDto BEntree) {
	        List<String> errors = BonEntreeFournisseurValidator.validate(BEntree);

	        if (!errors.isEmpty()) {
	            log.error("Commande fournisseur n'est pas valide");
	            throw new InvalidEntityException("La commande fournisseur n'est pas valide");
	        }

	        if (BEntree.getId() != null && BEntree.isCommandeLivree()) {
	            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree");
	        }

	        // Save the BonEntree to the database (assuming you have a repository for this)
	        BonEntree savedBonEntree = bonEntreeRepository.save(BonEntreeDto.toEntity(BEntree));
	        
	        // Send WebSocket notification
	        notificationController.sendNotification("Commande " + savedBonEntree.getId() + " saved. Please update status.");
	        return BonEntreeDto.fromEntity(savedBonEntree);

	        
	    }*/
	
	
	



@Override
public BonEntreeDto findById(Integer id) {
  if (id == null) {
    log.error("Commande fournisseur ID is NULL");
    return null;
  }
  return bonEntreeRepository.findById(id)
      .map(BonEntreeDto::fromEntity)
      .orElseThrow(() -> new EntityNotFoundException(
          "Aucune commande fournisseur n'a ete trouve avec l'ID " + id));
}

		
		@Override
		public BonEntreeDto findByCode(String code) {
		    return bonEntreeRepository.findBonEntreeByCode(code)
		    		.map(BonEntreeDto::fromEntity)
		    		.orElseThrow(() -> new EntityNotFoundException(
		            "Aucune commande fournisseur n'a ete trouve avec le CODE " + code));
		}
	

		@Override
		public void delete(Integer id) {
				    if (id == null) {
				      log.error("Commande fournisseur ID is NULL");
				      return;
				    }
				    List<LigneEntree> ligneEntrees = ligneEntreeFournisseurRepository.findAllByBonEntreeId(id);
				    if (!ligneEntrees.isEmpty()) {
				      throw new InvalidOperationException("Impossible de supprimer une commande fournisseur deja utilise");
				    }
				    bonEntreeRepository.deleteById(id);
				  
			
		}
		

		@Override
		public List<BonEntreeDto> findAll() {
			return bonEntreeRepository.findAll().stream()
			        .map(BonEntreeDto::fromEntity)
			        .collect(Collectors.toList());
		}
		

		@Override
		public BonEntreeDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
			checkIdCommande(idCommande);
			checkEtatCommande(idCommande);		 
		    BonEntreeDto commandeFournisseur = checkEtatCommande(idCommande);
		    commandeFournisseur.setEtatCommande(etatCommande);

		    BonEntree savedCommande = bonEntreeRepository.save(BonEntreeDto.toEntity(commandeFournisseur));

		    if (commandeFournisseur.isCommandeLivree()) {
		        updateMvtStk(idCommande);
		    }
		    return BonEntreeDto.fromEntity(savedCommande);
		   
		}

		  private BonEntreeDto checkEtatCommande(Integer idCommande) {
			  BonEntreeDto commandeFournisseur = findById(idCommande);
			    if (commandeFournisseur.isCommandeLivree()) {
			      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree");
			    }
			    return commandeFournisseur;
			  }
		
		  private void checkIdCommande(Integer idCommande) {
			    if (idCommande == null) {
			      log.error("Commande fournisseur ID is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null");
			    }
			  }
		  private void updateMvtStk(Integer idCommande) {
			    List<LigneEntree> ligneCommandeFournisseur = ligneEntreeFournisseurRepository.findAllByBonEntreeId(idCommande);
			    ligneCommandeFournisseur.forEach(lig -> {
			      effectuerEntree(lig);
			    });
			  }




		  private void checkIdLigneCommande(Integer idligneEntreeFournisseur) {
			    if (idligneEntreeFournisseur == null) {
			      log.error("L'ID de la ligne commande is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null");
			    }
			  }
		  
		  @Override
		  public BonEntreeDto updateQuantiteCommande(Integer idCommande, Integer idligneEntreeFournisseur, Integer quantite) {
		    checkIdCommande(idCommande);
		    checkIdLigneCommande(idligneEntreeFournisseur);

		    if (quantite == null || quantite == 0) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO");
		    }

		    BonEntreeDto commandeFournisseur = checkEtatCommande(idCommande);
		    Optional<LigneEntree> ligneFournisseurOptional = findLigneCommandeFournisseur(idligneEntreeFournisseur);

		    LigneEntree ligneFounisseur = ligneFournisseurOptional.get();
		    ligneFounisseur.setQuantite(quantite);
		    ligneEntreeFournisseurRepository.save(ligneFounisseur);

		    return commandeFournisseur;
		  }		 

		  private Optional<LigneEntree> findLigneCommandeFournisseur(Integer idligneEntreeFournisseur) {
			    Optional<LigneEntree> ligneCommandeFournisseurOptional = ligneEntreeFournisseurRepository.findById(idligneEntreeFournisseur);
			    if (ligneCommandeFournisseurOptional.isEmpty()) {
			      throw new EntityNotFoundException(
			          "Aucune ligne commande fournisseur n'a ete trouve avec l'ID " + idligneEntreeFournisseur);
			    }
			    return ligneCommandeFournisseurOptional;
			  }


		  public BonEntreeDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
		    checkIdCommande(idCommande);
		    if (idFournisseur == null) {
		      log.error("L'ID du fournisseur is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID fournisseur null");
		    }
		    BonEntreeDto commandeFournisseur = checkEtatCommande(idCommande);
		    Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
		    if (fournisseurOptional.isEmpty()) {
		      throw new EntityNotFoundException(
		          "Aucun fournisseur n'a ete trouve avec l'ID " + idFournisseur);
		    }
		    commandeFournisseur.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));

		    return BonEntreeDto.fromEntity(
		    		bonEntreeRepository.save(BonEntreeDto.toEntity(commandeFournisseur))
		    );
		  }
		
		  
		  public BonEntreeDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
			    checkIdCommande(idCommande);
			    checkIdLigneCommande(idLigneCommande);
			    checkIdArticle(idArticle, "nouvel");

			    BonEntreeDto commandeFournisseur = checkEtatCommande(idCommande);

			    Optional<LigneEntree> ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);

			    Optional<Article> articleOptional = articleRepository.findById(idArticle);
			    if (articleOptional.isEmpty()) {
			      throw new EntityNotFoundException(
			          "Aucune article n'a ete trouve avec l'ID " + idArticle);
			    }

			    List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
			    if (!errors.isEmpty()) {
			      throw new InvalidEntityException("Article invalid", errors);
			    }

			    LigneEntree ligneCommandeFournisseurToSaved = ligneCommandeFournisseur.get();
			    ligneCommandeFournisseurToSaved.setArticle(articleOptional.get());
			    ligneEntreeFournisseurRepository.save(ligneCommandeFournisseurToSaved);

			    return commandeFournisseur;
			  }

		  
		  
		  private void checkIdArticle(Integer idArticle, String msg) {
			    if (idArticle == null) {
			      log.error("L'ID de " + msg + " is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null");
			    }
			  }
	
		  
		  public BonEntreeDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
		    checkIdCommande(idCommande);
		    checkIdLigneCommande(idLigneCommande);

		    BonEntreeDto commandeFournisseur = checkEtatCommande(idCommande);
		    // Just to check the LigneCommandeFournisseur and inform the fournisseur in case it is absent
		    findLigneCommandeFournisseur(idLigneCommande);
		    ligneEntreeFournisseurRepository.deleteById(idLigneCommande);

		    return commandeFournisseur;
		  }	 
		  
		  public List<LigneEntreeDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
			    return ligneEntreeFournisseurRepository.findAllByBonEntreeId(idCommande).stream()
			        .map(LigneEntreeDto::fromEntity)
			        .collect(Collectors.toList());
			  }
		  
		  
		  public BonEntreeDto deleteBonEntree(Integer id) {
			    // Find the BonEntree entry
			    Optional<BonEntree> optionalBonEntree = bonEntreeRepository.findById(id);
			    if (optionalBonEntree.isEmpty()) {
			        throw new EntityNotFoundException("Aucun BonEntree avec l'ID " + id + " n'a ete trouve dans la BDD");
			    }

			    BonEntree bonEntree = optionalBonEntree.get();

			    // Delete related LigneEntrees
			    if (bonEntree.getLigneEntrees() != null) {
			        bonEntree.getLigneEntrees().forEach(ligneEntree -> {
			            ligneEntreeFournisseurRepository.deleteById(ligneEntree.getId());
			        });
			    }

			    // Delete the BonEntree entry
			    bonEntreeRepository.deleteById(id);

			    // Optionally return the deleted BonEntreeDto
			    return BonEntreeDto.fromEntity(bonEntree);
			}

}