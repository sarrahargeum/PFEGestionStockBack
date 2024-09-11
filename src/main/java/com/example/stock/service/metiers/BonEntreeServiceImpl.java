package com.example.stock.service.metiers;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import com.example.stock.model.Notification;
import com.example.stock.model.TypeStock;
import com.example.stock.repository.ArticleRepository;
import com.example.stock.repository.BonEntreeRepository;
import com.example.stock.repository.FournisseurRepository;
import com.example.stock.repository.LigneEntreeRepository;
import com.example.stock.service.BonEntreeService;
import com.example.stock.service.MVTStockService;
import com.example.stock.validator.ArticleValidator;
import com.example.stock.validator.BonEntreeFournisseurValidator;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
	@Autowired
    NotificationServiceImpl notificationService;
	

	@Transactional
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
	        throw new EntityNotFoundException("Aucun fournisseur avec l'ID " + BEntree.getFournisseur().getId() + " n'a ete trouve dans la BDD");
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
	                articleErrors.add("Impossible d'enregister une commande avec un article NULL");
	            }
	        });
	    }

	    if (!articleErrors.isEmpty()) {
	        log.warn("");
	        throw new InvalidEntityException("Article n'existe pas dans la BDD");
	    }

	    BEntree.setDateCommande(Instant.now());

	    // Enregistrer la commande sans le code pour le moment
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
	    bonEntreeRepository.save(savedCmdFrs); 

	    if (Objects.nonNull(savedCmdFrs)) {
	        String notificationMessage = "Commande " + savedCmdFrs.getCode() + " en preparation. Please valider cette commande Entree.";
	        notificationController.sendOrderValidationNotification(notificationMessage);

	        Notification notification = new Notification();
	        notification.setMessage(notificationMessage);
	        notification.setDateNotification(Instant.now());
	        notification.setEtatNotification(false);
	        notification.setType("Validation");
	        notification.setCodeCommande(savedCmdFrs.getCode());
	        notificationService.save(notification);
	    }

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
	
@Override
public BonEntreeDto findById(Integer id) {
    if (id == null) {
        log.error("Commande fournisseur ID is NULL");
        return null;
    }
    return bonEntreeRepository.findById(id)
        .map(bonEntree -> {
            BonEntreeDto dto = BonEntreeDto.fromEntity(bonEntree);
            dto.setLigneEntrees(
                bonEntree.getLigneEntrees() != null ?
                bonEntree.getLigneEntrees().stream()
                    .map(LigneEntreeDto::fromEntity)
                    .collect(Collectors.toList()) : null
            );
            return dto;
        })
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
		  
		  @Transactional
		  public BonEntreeDto deleteBonEntree(Integer id) {
			    // Find the BonEntree entry
			    Optional<BonEntree> optionalBonEntree = bonEntreeRepository.findById(id);
			    if (optionalBonEntree.isEmpty()) {
			        throw new EntityNotFoundException("Aucun BonEntree avec l'ID " + id + " n'a ete trouve dans la BDD");
			    }

			    BonEntree bonEntree = optionalBonEntree.get();
			      ligneEntreeFournisseurRepository.deleteByBonEntree(bonEntree);
			    bonEntreeRepository.delete(bonEntree);

			    return null;
			}
		 
		  
		  public void generateEcelBE(HttpServletResponse response) throws IOException {
			  // Fetch all BonEntree entities
			    List<BonEntree> bonEntrees = bonEntreeRepository.findAll();

			    // Create a new workbook and sheet
			    Workbook workbook = new HSSFWorkbook();
			    Sheet sheet = workbook.createSheet("Bon Entree Info");

			    // Create header row
			    Row headerRow = sheet.createRow(0);
			    headerRow.createCell(0).setCellValue("ID");
			    headerRow.createCell(1).setCellValue("Code");
			    headerRow.createCell(2).setCellValue("Date Commande");
			    headerRow.createCell(3).setCellValue("Etat Commande");
			    headerRow.createCell(4).setCellValue("Fournisseur");

			    // Fill data rows
			    int dataRowIndex = 1;
			    for (BonEntree bonEntree : bonEntrees) {
			        Row dataRow = sheet.createRow(dataRowIndex++);
			        dataRow.createCell(0).setCellValue(bonEntree.getId());
			        dataRow.createCell(1).setCellValue(bonEntree.getCode());
			        dataRow.createCell(2).setCellValue(bonEntree.getDateCommande().toString());
			        dataRow.createCell(3).setCellValue(bonEntree.getEtatCommande().name());
			        dataRow.createCell(4).setCellValue(bonEntree.getFournisseur().getNom()); 
			    }

			    response.setContentType("application/vnd.ms-excel");
			    response.setHeader("Content-Disposition", "attachment;filename=BonEntreeInfo.xls");

			    try (ServletOutputStream ops = response.getOutputStream()) {
			        workbook.write(ops);
			    } catch (IOException e) {
			        e.printStackTrace();
			    } finally {
			        workbook.close();
			    }
		  }
		  
		  
			@Override
			public long countBonEntrees() {
				return bonEntreeRepository.count();
				    
			}

}