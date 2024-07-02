package com.example.stock.service.metiers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.exception.InvalidOperationException;
import com.example.stock.model.Article;
import com.example.stock.model.BonEntree;
import com.example.stock.model.EtatCommande;
import com.example.stock.model.Fournisseur;
import com.example.stock.model.LigneEntree;
import com.example.stock.model.MVTStock;
import com.example.stock.model.TypeStock;
import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.BonEntreeRepository;
import com.example.stock.repository.FournisseurRepository;
import com.example.stock.repository.LigneEntreeRepository;
import com.example.stock.service.BonEntreeService;
import com.example.stock.service.MVTStockService;
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


	@Override
	public BonEntree save(BonEntree bonentreFourni) {

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
	    if (bonentreFourni.getLigneEntrees() != null) {
	        bonentreFourni.getLigneEntrees().forEach(ligCmdFrs -> {
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
	    BonEntree savedCmdFrs = bonEntreeRepository.save(bonentreFourni);

	    if (bonentreFourni.getLigneEntrees() != null) {
	        bonentreFourni.getLigneEntrees().forEach(ligCmdFrs -> {
	            LigneEntree ligneEntree = new LigneEntree();
	            ligneEntree.setBonEntree(savedCmdFrs);
	            ligneEntree.setArticle(ligCmdFrs.getArticle());
	            ligneEntree.setQuantite(ligCmdFrs.getQuantite());
	            ligneEntree.setIdMagasin(savedCmdFrs.getIdMagasin());

	            LigneEntree savedLigne = ligneEntreeFournisseurRepository.save(ligneEntree);

	            effectuerEntree(savedLigne);
	        });
	    }

	    return savedCmdFrs;
	}

private void effectuerEntree(LigneEntree lig) {
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
		public BonEntree findById(Integer id) {
			 if (id == null) {
			      log.error("Commande fournisseur ID is NULL");
			      return null;
			    }
			    return bonEntreeRepository.findById(id)
			        .orElseThrow(() -> new EntityNotFoundException(
			            "Aucune commande fournisseur n'a ete trouve avec l'ID " + id ));
		}

		
		@Override
		public BonEntree findByCode(String code) {
		    return bonEntreeRepository.findBonEntreeByCode(code)
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
		public List<BonEntree> findAll() {
			  return bonEntreeRepository.findAll().stream()
				        .collect(Collectors.toList());
		}
	

		@Override
		public BonEntree updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
			checkIdCommande(idCommande);
			checkEtatCommande(idCommande);		 
		    BonEntree commandeFournisseur = checkEtatCommande(idCommande);
		    commandeFournisseur.setEtatCommande(etatCommande);

		    BonEntree savedCommande = bonEntreeRepository.save(commandeFournisseur);

		    if (commandeFournisseur.isBonFournisseurLivree()) {
		        updateMvtStk(idCommande);
		    }

		    return savedCommande;
		}

		private BonEntree checkEtatCommande(Integer idCommande) {
		    return bonEntreeRepository.findById(idCommande)
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
		    List<LigneEntree> ligneEntreeFournisseurs = ligneEntreeFournisseurRepository.findAllByBonEntreeId(idCommande);
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
		  public BonEntree updateQuantiteCommande(Integer idCommande, Integer idligneEntreeFournisseur, Integer quantite) {
		    checkIdCommande(idCommande);
		    checkIdLigneCommande(idligneEntreeFournisseur);

		    if (quantite == null || quantite == 0) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO");
		    }

		    BonEntree commandeFournisseur = checkEtatCommande(idCommande);
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

		  
}