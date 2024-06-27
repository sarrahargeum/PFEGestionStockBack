package com.example.stock.service.metiers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.exception.InvalidEntityException;
import com.example.stock.exception.InvalidOperationException;
import com.example.stock.model.Article;
import com.example.stock.model.BonEntreFournisseur;
import com.example.stock.model.EtatCommande;
import com.example.stock.model.Fournisseur;
import com.example.stock.model.LigneEntreeFournisseur;
import com.example.stock.model.MVTStock;
import com.example.stock.model.TypeStock;
import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.BonEntreFournisseurRepository;
import com.example.stock.repository.FournisseurRepository;
import com.example.stock.repository.LigneEntreeFournisseurRepository;
import com.example.stock.service.BonEntreFournisseurService;
import com.example.stock.service.MVTStockService;
import com.example.stock.validator.BonEntreeFournisseurValidator;

import ch.qos.logback.core.spi.ErrorCodes;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BonEntreFournisseurServiceImpl implements BonEntreFournisseurService{
	
	
	@Autowired
	BonEntreFournisseurRepository bonEntreFournisseurRepository;

	@Autowired
	FournisseurRepository fournisseurRepository;
	
	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	LigneEntreeFournisseurRepository ligneEntreeFournisseurRepository;
	
	@Autowired
	MVTStockService mvtStockService;


	@Override
	public BonEntreFournisseur save(BonEntreFournisseur bonentreFourni) {

	    List<String> errors = BonEntreeFournisseurValidator.validate(bonentreFourni);

	    if (bonentreFourni.getFournisseur() == null || bonentreFourni.getFournisseur().getId() == null) {
	        errors.add("Fournisseur is required and must have a valid ID");
	    } else {
	        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(bonentreFourni.getFournisseur().getId());
	        if (fournisseur.isEmpty()) {
	            errors.add("Fournisseur with ID " + bonentreFourni.getFournisseur().getId() + " does not exist");
	        } else {
	            bonentreFourni.setFournisseur(fournisseur.get());
	        }
	    }

	  

	    List<String> articleErrors = new ArrayList<>();
	    if (bonentreFourni.getLigneEntreeFournisseurs() != null) {
	        bonentreFourni.getLigneEntreeFournisseurs().forEach(ligCmdFrs -> {
	            if (ligCmdFrs.getArticle() != null) {
	                Optional<Article> article = articleRepository.findById(ligCmdFrs.getArticle().getId());
	                if (article.isEmpty()) {
	                    articleErrors.add("L'article avec l'ID " + ligCmdFrs.getArticle().getId() + " n'existe pas");
	                }
	            } else {
	                articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
	            }
	        });
	    }

	   
	    bonentreFourni.setDateCommande(Instant.now());
	    BonEntreFournisseur savedCmdFrs = bonEntreFournisseurRepository.save(bonentreFourni);

	    if (bonentreFourni.getLigneEntreeFournisseurs() != null) {
	        bonentreFourni.getLigneEntreeFournisseurs().forEach(ligCmdFrs -> {
	            LigneEntreeFournisseur ligneEntreeFournisseur = new LigneEntreeFournisseur();
	            ligneEntreeFournisseur.setBonEntreFournisseur(savedCmdFrs);
	            ligneEntreeFournisseur.setArticle(ligCmdFrs.getArticle());
	            ligneEntreeFournisseur.setQuantite(ligCmdFrs.getQuantite());
	            ligneEntreeFournisseur.setIdMagasin(savedCmdFrs.getIdMagasin());

	            LigneEntreeFournisseur savedLigne = ligneEntreeFournisseurRepository.save(ligneEntreeFournisseur);

	            effectuerEntree(savedLigne);
	        });
	    }

	    return savedCmdFrs;
	}

private void effectuerEntree(LigneEntreeFournisseur lig) {
    MVTStock mvtStkDto = MVTStock.builder()
        .article(lig.getArticle())
        .dateMvt(Instant.now())
        .typestock(TypeStock.ENTREE)
        .quantite(lig.getQuantite())
        .idMagasin(lig.getIdMagasin())
        .build();
    mvtStockService.entreeStock(mvtStkDto);
}

	  

		@Override
		public BonEntreFournisseur findById(Integer id) {
			 if (id == null) {
			      log.error("Commande fournisseur ID is NULL");
			      return null;
			    }
			    return bonEntreFournisseurRepository.findById(id)
			        .orElseThrow(() -> new EntityNotFoundException(
			            "Aucune commande fournisseur n'a ete trouve avec l'ID " + id ));
		}

		
		@Override
		public BonEntreFournisseur findByCode(String code) {
		    return bonEntreFournisseurRepository.findBonEntreFournisseurByCode(code)
		        .orElseThrow(() -> new EntityNotFoundException(
		            "Aucune commande fournisseur n'a ete trouve avec le CODE " + code));
		}

		

		@Override
		public void delete(Integer id) {
				    if (id == null) {
				      log.error("Commande fournisseur ID is NULL");
				      return;
				    }
				    List<LigneEntreeFournisseur> ligneEntreeFournisseurs = ligneEntreeFournisseurRepository.findAllByBonEntreFournisseurId(id);
				    if (!ligneEntreeFournisseurs.isEmpty()) {
				      throw new InvalidOperationException("Impossible de supprimer une commande fournisseur deja utilise");
				    }
				    bonEntreFournisseurRepository.deleteById(id);
				  
			
		}
		

		@Override
		public List<BonEntreFournisseur> findAll() {
			  return bonEntreFournisseurRepository.findAll().stream()
				        .collect(Collectors.toList());
		}
	

		@Override
		public BonEntreFournisseur updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
			checkIdCommande(idCommande);
			checkEtatCommande(idCommande);		 
		    BonEntreFournisseur commandeFournisseur = checkEtatCommande(idCommande);
		    commandeFournisseur.setEtatCommande(etatCommande);

		    BonEntreFournisseur savedCommande = bonEntreFournisseurRepository.save(commandeFournisseur);

		    if (commandeFournisseur.isBonFournisseurLivree()) {
		        updateMvtStk(idCommande);
		    }

		    return savedCommande;
		}

		private BonEntreFournisseur checkEtatCommande(Integer idCommande) {
		    return bonEntreFournisseurRepository.findById(idCommande)
		        .orElseThrow(() -> new EntityNotFoundException(
		            "Aucune commande fournisseur n'a ete trouve avec l'ID " + idCommande));
		}
		
		  private void checkIdCommande(Integer idCommande) {
			    if (idCommande == null) {
			      log.error("Commande fournisseur ID is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null");
			    }
			  }

		private void updateMvtStk(Integer idCommande) {
		    List<LigneEntreeFournisseur> ligneEntreeFournisseurs = ligneEntreeFournisseurRepository.findAllByBonEntreFournisseurId(idCommande);
		    ligneEntreeFournisseurs.forEach(lig -> {
		        MVTStock mvtStk = MVTStock.builder()
		            .article(lig.getArticle())
		            .dateMvt(Instant.now())
		            .typestock(TypeStock.ENTREE)
		            .quantite(lig.getQuantite())
		            .idMagasin(lig.getIdMagasin())
		            .build();
		        mvtStockService.entreeStock(mvtStk);
		    });
		}

		  private void checkIdLigneCommande(Integer idligneEntreeFournisseur) {
			    if (idligneEntreeFournisseur == null) {
			      log.error("L'ID de la ligne commande is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null");
			    }
			  }
		  
		  @Override
		  public BonEntreFournisseur updateQuantiteCommande(Integer idCommande, Integer idligneEntreeFournisseur, Integer quantite) {
		    checkIdCommande(idCommande);
		    checkIdLigneCommande(idligneEntreeFournisseur);

		    if (quantite == null || quantite == 0) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO");
		    }

		    BonEntreFournisseur commandeFournisseur = checkEtatCommande(idCommande);
		    Optional<LigneEntreeFournisseur> ligneFournisseurOptional = findLigneCommandeFournisseur(idligneEntreeFournisseur);

		    LigneEntreeFournisseur ligneFounisseur = ligneFournisseurOptional.get();
		    ligneFounisseur.setQuantite(quantite);
		    ligneEntreeFournisseurRepository.save(ligneFounisseur);

		    return commandeFournisseur;
		  }		 

		  private Optional<LigneEntreeFournisseur> findLigneCommandeFournisseur(Integer idligneEntreeFournisseur) {
			    Optional<LigneEntreeFournisseur> ligneCommandeFournisseurOptional = ligneEntreeFournisseurRepository.findById(idligneEntreeFournisseur);
			    if (ligneCommandeFournisseurOptional.isEmpty()) {
			      throw new EntityNotFoundException(
			          "Aucune ligne commande fournisseur n'a ete trouve avec l'ID " + idligneEntreeFournisseur);
			    }
			    return ligneCommandeFournisseurOptional;
			  }

		  
}