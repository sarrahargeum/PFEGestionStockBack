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
import com.example.stock.model.BonSortie;
import com.example.stock.model.Client;
import com.example.stock.model.EtatCommande;
import com.example.stock.model.Fournisseur;
import com.example.stock.model.LigneEntree;
import com.example.stock.model.LigneSortie;
import com.example.stock.model.MVTStock;
import com.example.stock.model.TypeStock;
import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.BonEntreeRepository;
import com.example.stock.repository.BonSortieRepository;
import com.example.stock.repository.ClientRepository;
import com.example.stock.repository.FournisseurRepository;
import com.example.stock.repository.LigneEntreeRepository;
import com.example.stock.repository.LigneSortieRepository;
import com.example.stock.service.BonEntreeService;
import com.example.stock.service.BonSortieService;
import com.example.stock.service.MVTStockService;
import com.example.stock.validator.BonEntreeFournisseurValidator;
import com.example.stock.validator.BonSortieValidator;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BonSortieServiceImpl implements BonSortieService {
	
	@Autowired
	BonSortieRepository bonSortieRepository;

	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	LigneSortieRepository ligneSortieRepository;
	
	@Autowired
	MVTStockService mvtStockService;


	@Override
	public BonSortie save(BonSortie bonSortie) {

	    List<String> errors = BonSortieValidator.validate(bonSortie);

	    if (bonSortie.getClient() == null || bonSortie.getClient().getId() == null) {
	        errors.add("Client is required and must have a valid ID");
	    } else {
	        Optional<Client> client = clientRepository.findById(bonSortie.getClient().getId());
	        if (client.isEmpty()) {
	            errors.add("client with ID " + bonSortie.getClient().getId() + " does not exist");
	        } else {
	            bonSortie.setClient(client.get());
	        }
	    }

	  

	    List<String> articleErrors = new ArrayList<>();
	    if (bonSortie.getLigneSorties() != null) {
	    	bonSortie.getLigneSorties().forEach(ligCmdCLts -> {
	            if (ligCmdCLts.getArticle() != null) {
	                Optional<Article> article = articleRepository.findById(ligCmdCLts.getArticle().getId());
	                if (article.isEmpty()) {
	                    articleErrors.add("L'article avec l'ID " + ligCmdCLts.getArticle().getId() + " n'existe pas");
	                }
	            } else {
	                articleErrors.add("Impossible d'enregistrer une commande avec un article NULL");
	            }
	        });
	    }

	   
	    bonSortie.setDateCommande(Instant.now());
	    BonSortie savedBonSortie  = bonSortieRepository.save(bonSortie);
	  
	    if (bonSortie.getLigneSorties() != null) {
	            bonSortie.getLigneSorties().forEach(ligneCmdSortie -> {
	                LigneSortie ligneClient = new LigneSortie(); 
	                
	                ligneClient.setBonSortie(savedBonSortie);
	                ligneClient.setArticle(ligneCmdSortie.getArticle());
	                ligneClient.setPrixUnitaire(ligneCmdSortie.getPrixUnitaire());
	                ligneClient.setQuantite(ligneCmdSortie.getQuantite());
	                ligneClient.setIdMagasin(bonSortie.getIdMagasin()); 
	                ligneSortieRepository.save(ligneClient);
	            });
	        }
	        return savedBonSortie;
	    }


private void effectuerSortie(LigneSortie lig) {
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
		public BonSortie findById(Integer id) {
			 if (id == null) {
			      log.error("Commande client ID is NULL");
			      return null;
			    }
			    return bonSortieRepository.findById(id)
			        .orElseThrow(() -> new EntityNotFoundException(
			            "Aucune commande client n'a ete trouve avec l'ID " + id ));
		}

		
		@Override
		public BonSortie findByCode(String code) {
		    return bonSortieRepository.findBonSortieByCode(code)
		        .orElseThrow(() -> new EntityNotFoundException(
		            "Aucune commande client n'a ete trouve avec le CODE " + code));
		}

		

		@Override
		public void delete(Integer id) {
				    if (id == null) {
				      log.error("Commande fournisseur ID is NULL");
				      return;
				    }
				    List<LigneSortie> ligneSorties = ligneSortieRepository.findAllByBonSortieId(id);
				    if (!ligneSorties.isEmpty()) {
				      throw new InvalidOperationException("Impossible de supprimer une commande fournisseur deja utilise");
				    }
				    bonSortieRepository.deleteById(id);
				  
			
		}
		

		@Override
		public List<BonSortie> findAll() {
			  return bonSortieRepository.findAll().stream()
				        .collect(Collectors.toList());
		}
	

		@Override
		public BonSortie updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
			checkIdCommande(idCommande);
			checkEtatCommande(idCommande);		 
			BonSortie commandeCli = checkEtatCommande(idCommande);
		    commandeCli.setEtatCommande(etatCommande);

		    BonSortie savedCommande = bonSortieRepository.save(commandeCli);

		    if (commandeCli.isBonSortieLivree()) {
		        updateMvtStk(idCommande);
		    }

		    return savedCommande;
		}

		private BonSortie checkEtatCommande(Integer idCommande) {
		    return bonSortieRepository.findById(idCommande)
		        .orElseThrow(() -> new EntityNotFoundException(
		            "Aucune commande client  n'a ete trouve avec l'ID " + idCommande));
		}
		
		  private void checkIdCommande(Integer idCommande) {
			    if (idCommande == null) {
			      log.error("Commande fournisseur ID is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null");
			    }
			  }

		private void updateMvtStk(Integer idCommande) {
		    List<LigneSortie> ligneSorties = ligneSortieRepository.findAllByBonSortieId(idCommande);
		    ligneSorties.forEach(lig -> {
		        MVTStock mvtStk = MVTStock.builder()
		            .article(lig.getArticle())
		            .dateMvt(Instant.now())
		            .typestock(TypeStock.SORTIE)
		            .quantite(lig.getQuantite())
		            .idMagasin(lig.getIdMagasin())
		            .build();
		        mvtStockService.sortieStock(mvtStk);
		    });
		}

		  private void checkIdLigneCommande(Integer idligneSortie) {
			    if (idligneSortie == null) {
			      log.error("L'ID de la ligne commande is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null");
			    }
			  }
		  
		  @Override
		  public BonSortie updateQuantiteCommande(Integer idCommande, Integer idligneSortie, Integer quantite) {
		    checkIdCommande(idCommande);
		    checkIdLigneCommande(idligneSortie);

		    if (quantite == null || quantite == 0) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO");
		    }

		    BonSortie commandeClient = checkEtatCommande(idCommande);
		    Optional<LigneSortie> ligneSortieOptional = findLigneCommandeClient(idligneSortie);

		    LigneSortie ligneSortie = ligneSortieOptional.get();
		    ligneSortie.setQuantite(quantite);
		    ligneSortieRepository.save(ligneSortie);

		    return commandeClient;
		  }		 

		  private Optional<LigneSortie> findLigneCommandeClient(Integer idligneSortie) {
			    Optional<LigneSortie> ligneCommandeClientOptional = ligneSortieRepository.findById(idligneSortie);
			    if (ligneCommandeClientOptional.isEmpty()) {
			      throw new EntityNotFoundException(
			          "Aucune ligne commande client n'a ete trouve avec l'ID " + idligneSortie);
			    }
			    return ligneCommandeClientOptional;
			  }

		  


}
