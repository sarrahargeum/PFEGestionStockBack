package com.example.stock.service.metiers;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.controller.NotificationController;
import com.example.stock.dto.ArticleDto;
import com.example.stock.dto.BonEntreeDto;
import com.example.stock.dto.BonSortieDto;
import com.example.stock.dto.ClientDto;
import com.example.stock.dto.LigneEntreeDto;
import com.example.stock.dto.LigneSortieDto;
import com.example.stock.dto.MVTStockDto;
import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.exception.InvalidEntityException;
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
import com.example.stock.validator.ArticleValidator;
import com.example.stock.validator.BonEntreeFournisseurValidator;
import com.example.stock.validator.BonSortieValidator;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
	
	@Autowired
	NotificationController notificationController;

	

	

	 public BonSortieDto save(BonSortieDto BSortie) {

		 List<String> errors = BonSortieValidator.validate(BSortie);

		 if (!errors.isEmpty()) {
		      log.error("Commande client n'est pas valide");
		      throw new InvalidEntityException("La commande client n'est pas valide");
		    }

		    if (BSortie.getId() != null && BSortie.isCommandeLivree()) {
		      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree");
		    }

		    Optional<Client> client = clientRepository.findById(BSortie.getClient().getId());
		    if (client.isEmpty()) {
		      log.warn("Client with ID {} was not found in the DB", BSortie.getClient().getId());
		      throw new EntityNotFoundException("Aucun client avec l'ID" + BSortie.getClient().getId() + " n'a ete trouve dans la BDD");
		    }

		    List<String> articleErrors = new ArrayList<>();

		    if (BSortie.getLigneSorties() != null) {
		    	BSortie.getLigneSorties().forEach(ligCmdCls -> {
		        if (ligCmdCls.getArticle() != null) {
		          Optional<Article> article = articleRepository.findById(ligCmdCls.getArticle().getId());
		          if (article.isEmpty()) {
		            articleErrors.add("L'article avec l'ID " + ligCmdCls.getArticle().getId() + " n'existe pas");
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
		    BSortie.setDateCommande(Instant.now());
		    
		    BonSortie savedCmdCls = bonSortieRepository.save(BonSortieDto.toEntity(BSortie));

		    if (BSortie.getLigneSorties() != null) {
		    	BSortie.getLigneSorties().forEach(ligCmdClt -> {
		          LigneSortie ligneCommandeClient = LigneSortieDto.toEntity(ligCmdClt);
		          ligneCommandeClient.setBonSortie(savedCmdCls);
		          ligneCommandeClient.setIdMagasin(BSortie.getIdMagasin());
		          LigneSortie savedLigneCmd = ligneSortieRepository.save(ligneCommandeClient);

		          effectuerSortie(savedLigneCmd);
		        });
		      }
		      // Send WebSocket notification
	      //  notificationController.sendNotification("Commande " + savedCmdCls.getId() + " saved. Please update status.");
	        return BonSortieDto.fromEntity(savedCmdCls);
	}
	 
	  private void effectuerSortie(LigneSortie lig) {
		    MVTStockDto mvtStkDto = MVTStockDto.builder()
		        .article(ArticleDto.fromEntity(lig.getArticle()))
		        .dateMvt(Instant.now())
		        .typeMvt(TypeStock.SORTIE)
		        .quantite(lig.getQuantite())
		        .idMagasin(lig.getIdMagasin())
		        .build();
		    mvtStockService.sortieStock(mvtStkDto);
		  }
		

	
		
		@Override
		public BonSortieDto findByCode(String code) {
		    return bonSortieRepository.findBonSortieByCode(code)
		    		.map(BonSortieDto::fromEntity)
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
		public List<BonSortieDto> findAll() {

			return bonSortieRepository.findAll().stream()
			        .map(BonSortieDto::fromEntity)
			        .collect(Collectors.toList());
	
		}
	

		@Override
		public BonSortieDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
			checkIdCommande(idCommande);
			checkEtatCommande(idCommande);		 
			BonSortieDto commandeClient = checkEtatCommande(idCommande);
			commandeClient.setEtatCommande(etatCommande);

		    BonSortie savedCommande = bonSortieRepository.save(BonSortieDto.toEntity(commandeClient));

		    if (commandeClient.isCommandeLivree()) {
		        updateMvtStk(idCommande);
		    }
		    return BonSortieDto.fromEntity(savedCommande);
		}

		  private BonSortieDto checkEtatCommande(Integer idCommande) {
			  BonSortieDto commandeClient = findById(idCommande);
			    if (commandeClient.isCommandeLivree()) {
			      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree");
			    }
			    return commandeClient;
			  }
		
	
		
		  private void checkIdCommande(Integer idCommande) {
			    if (idCommande == null) {
			      log.error("Commande fournisseur ID is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null");
			    }
			  }

		  private void updateMvtStk(Integer idCommande) {
			    List<LigneSortie> ligneCommandeClients = ligneSortieRepository.findAllByBonSortieId(idCommande);
			    ligneCommandeClients.forEach(lig -> {
			     // effectuerSortie(lig);
			    });
			  }
		  
		

		  private void checkIdLigneCommande(Integer idligneSortie) {
			    if (idligneSortie == null) {
			      log.error("L'ID de la ligne commande is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null");
			    }
			  }
		  
		  @Override
		  public BonSortieDto updateQuantiteCommande(Integer idCommande, Integer idligneSortie, Integer quantite) {
		    checkIdCommande(idCommande);
		    checkIdLigneCommande(idligneSortie);

		    if (quantite == null || quantite == 0) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO");
		    }

		    BonSortieDto commandeClient = checkEtatCommande(idCommande);
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

		  @Override
		  public BonSortieDto updateClient(Integer idCommande, Integer idClient) {
			    checkIdCommande(idCommande);
			    if (idClient == null) {
			      log.error("L'ID du client is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID client null");
			    }
			    BonSortieDto commandeClient = checkEtatCommande(idCommande);
			    Optional<Client> clientOptional = clientRepository.findById(idClient);
			    if (clientOptional.isEmpty()) {
			      throw new EntityNotFoundException(
			          "Aucun client n'a ete trouve avec l'ID " + idClient);
			    }
			    commandeClient.setClient(ClientDto.fromEntity(clientOptional.get()));

			    return BonSortieDto.fromEntity(
			        bonSortieRepository.save(BonSortieDto.toEntity(commandeClient))
			    );
			  }

		  
		  @Override
		  public BonSortieDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
		    checkIdCommande(idCommande);
		    checkIdLigneCommande(idLigneCommande);
		    checkIdArticle(idArticle, "nouvel");

		    BonSortieDto commandeClient = checkEtatCommande(idCommande);

		    Optional<LigneSortie> ligneCommandeClient = findLigneCommandeClient(idLigneCommande);

		    Optional<Article> articleOptional = articleRepository.findById(idArticle);
		    if (articleOptional.isEmpty()) {
		      throw new EntityNotFoundException(
		          "Aucune article n'a ete trouve avec l'ID " + idArticle);
		    }

		    List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));
		    if (!errors.isEmpty()) {
		      throw new InvalidEntityException("Article invalid", errors);
		    }

		    LigneSortie ligneCommandeClientToSaved = ligneCommandeClient.get();
		    ligneCommandeClientToSaved.setArticle(articleOptional.get());
		    ligneSortieRepository.save(ligneCommandeClientToSaved);

		    return commandeClient;
		  }
		  
		  private void checkIdArticle(Integer idArticle, String msg) {
			    if (idArticle == null) {
			      log.error("L'ID de " + msg + " is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null");
			    }
		  }
		  
		  @Override
		  public BonSortieDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
		    checkIdCommande(idCommande);
		    checkIdLigneCommande(idLigneCommande);

		    BonSortieDto commandeClient = checkEtatCommande(idCommande);
		    // Just to check the LigneCommandeClient and inform the client in case it is absent
		    findLigneCommandeClient(idLigneCommande);
		    ligneSortieRepository.deleteById(idLigneCommande);

		    return commandeClient;
		  }	  
		  
		  @Override
		  public List<LigneSortieDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
		    return ligneSortieRepository.findAllByBonSortieId(idCommande).stream()
		        .map(LigneSortieDto::fromEntity)
		        .collect(Collectors.toList());
		  }
		  
		  @Transactional
		  public BonSortieDto deleteBonSortie(Integer id) {
			   
			  BonSortie bonSortie = bonSortieRepository.findById(id)
		                .orElseThrow(() -> new EntityNotFoundException("BonSortie not found"));

		        // Delete related LigneSortie entities
		        ligneSortieRepository.deleteByBonSortie(bonSortie);
		        // Now delete the BonSortie entity
		        bonSortieRepository.delete(bonSortie);
				return null;
			}

		  
		  @Override
		  public BonSortieDto findById(Integer id) {
		      if (id == null) {
		          log.error("BonSortie ID is NULL");
		          return null;
		      }

		      return bonSortieRepository.findById(id)
		          .map(bonSortie -> {
		              // Initialiser et charger les LigneSorties
		              Set<LigneSortie> ligneSortiesSet = bonSortie.getLigneSorties();
		              List<LigneSortieDto> ligneSortiesDtoList = new ArrayList<>();

		              if (ligneSortiesSet != null) {
		                  ligneSortiesDtoList = ligneSortiesSet.stream()
		                      .map(LigneSortieDto::fromEntity)
		                      .collect(Collectors.toList());
		              }

		              // Convertir BonSortie en BonSortieDto
		              BonSortieDto dto = BonSortieDto.fromEntity(bonSortie);

		              // Ajouter les LigneSorties converties au DTO
		              dto.setLigneSorties(ligneSortiesDtoList);

		              return dto;
		          })
		          .orElseThrow(() -> new EntityNotFoundException(
		              "Aucun BonSortie trouvé avec l'ID " + id
		          ));
		  }
		  
		  
		  
		  public BonSortieDto saveBSClient(BonSortieDto BSortie) {
			  List<String> errors = BonSortieValidator.validate(BSortie);

				 if (!errors.isEmpty()) {
				      log.error("Commande client n'est pas valide");
				      throw new InvalidEntityException("La commande client n'est pas valide");
				    }

				    if (BSortie.getId() != null && BSortie.isCommandeLivree()) {
				      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree");
				    }
				    Optional<Client> client = clientRepository.findById(BSortie.getClient().getId());
				    if (client.isEmpty()) {
				        ClientDto clientDto = BSortie.getClient();
				        Client newClient = ClientDto.toEntity(clientDto);
				        if (newClient.getIdMagasin() == null) {
				            throw new InvalidEntityException("Le client doit contenir toutes les informations requises");
				        }
				        Client savedClient = clientRepository.save(newClient);
				        BSortie.setClient(ClientDto.fromEntity(savedClient));
				    }
				    List<String> articleErrors = new ArrayList<>();

				    if (BSortie.getLigneSorties() != null) {
				    	BSortie.getLigneSorties().forEach(ligCmdCls -> {
				        if (ligCmdCls.getArticle() != null) {
				          Optional<Article> article = articleRepository.findById(ligCmdCls.getArticle().getId());
				          if (article.isEmpty()) {
				            articleErrors.add("L'article avec l'ID " + ligCmdCls.getArticle().getId() + " n'existe pas");
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
				    BSortie.setDateCommande(Instant.now());
				    
				    BonSortie savedCmdCls = bonSortieRepository.save(BonSortieDto.toEntity(BSortie));

				    if (BSortie.getLigneSorties() != null) {
				    	BSortie.getLigneSorties().forEach(ligCmdClt -> {
				          LigneSortie ligneCommandeClient = LigneSortieDto.toEntity(ligCmdClt);
				          ligneCommandeClient.setBonSortie(savedCmdCls);
				          ligneCommandeClient.setIdMagasin(BSortie.getIdMagasin());
				          LigneSortie savedLigneCmd = ligneSortieRepository.save(ligneCommandeClient);

				          effectuerSortie(savedLigneCmd);
				        });
				      }
				  
			        return BonSortieDto.fromEntity(savedCmdCls);
			}

	
}
