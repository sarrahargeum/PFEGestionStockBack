package com.example.stock.service.metiers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.dto.BonEntreeDto;
import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.exception.InvalidEntityException;
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
	 public BonEntree save(BonEntree BEntree) {

		 List<String> errors = BonEntreeFournisseurValidator.validate(BEntree);

		    if (!errors.isEmpty()) {
		      log.error("Commande fournisseur n'est pas valide");
		      throw new InvalidEntityException("La commande fournisseur n'est pas valide");
		    }

		    if (BEntree.getId() != null && BEntree.isBonFournisseurLivree()) {
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
		    
		    BonEntree savedCmdFrs = bonEntreeRepository.save(BonEntree.toEntity(BEntree));

		    if (BEntree.getLigneEntrees() != null) {
		    	BEntree.getLigneEntrees().forEach(ligCmdFrs -> {
		        LigneEntree ligneEntree = LigneEntree.toEntity(ligCmdFrs);
		        ligneEntree.setBonEntree(savedCmdFrs);
		        ligneEntree.setIdMagasin(savedCmdFrs.getIdMagasin());
		        LigneEntree saveLigne = ligneEntreeFournisseurRepository.save(ligneEntree);

		        effectuerEntree(saveLigne);
		      });
		    }
		    return BonEntree.fromEntity(savedCmdFrs);

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
		public List<BonEntreeDto> findAll() {
			return bonEntreeRepository.findAll().stream()
			        .map(BonEntreeDto::fromEntity)
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