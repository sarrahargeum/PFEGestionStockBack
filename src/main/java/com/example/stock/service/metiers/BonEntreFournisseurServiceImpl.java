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

    if (!errors.isEmpty()) {
        log.error("Bon fournisseur n'est pas valide");
        throw new InvalidEntityException("Le Bon fournisseur n'est pas valide", errors);
    }

    if (bonentreFourni.getId() != null && bonentreFourni.isBonFournisseurLivree()) {
        throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree");
    }

    Optional<Fournisseur> fournisseur = fournisseurRepository.findById(bonentreFourni.getFournisseur().getId());
    if (fournisseur.isEmpty()) {
        log.warn("Fournisseur with ID {} was not found in the DB", bonentreFourni.getFournisseur().getId());
        throw new EntityNotFoundException("Aucun fournisseur avec l'ID " + bonentreFourni.getFournisseur().getId() + " n'a ete trouve dans la BDD");
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

    if (!articleErrors.isEmpty()) {
        log.warn("Article validation errors: {}", String.join(", ", articleErrors));
        throw new InvalidEntityException("Article n'existe pas dans la BDD", articleErrors);
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
		    if (idCommande == null) {
		        log.error("Commande fournisseur ID is NULL");
		        throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null");
		    }

		    if (etatCommande == null) {
		        log.error("L'etat de la commande fournisseur is NULL");
		        throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null");
		    }

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

		  
		 

		  
		  
}