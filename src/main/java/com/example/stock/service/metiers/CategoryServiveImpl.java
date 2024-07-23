package com.example.stock.service.metiers;


import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.model.Article;
import com.example.stock.model.Category;
import com.example.stock.model.Client;
import com.example.stock.model.User;
import com.example.stock.repository.CategoryRepository;
import com.example.stock.service.CategoryService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiveImpl implements CategoryService {


    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category ajouterCategory(Category ca) {
   		 return categoryRepository.save(ca);
   	}

    
    @Override
    public List<Category> findAll() {
    	return categoryRepository.findAll().stream()
		        .collect(Collectors.toList());
    	    }


    public Category deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
        return null;
    }
    
    
    public Category retrieveCategory (Integer categoryId){
    	if (categoryId == null) {
            log.error("Ca&tegory ID is null");
            return null;
        }
        return categoryRepository.findById(categoryId).orElseThrow(() ->
        new EntityNotFoundException(
            "Aucun Category avec l'ID = " + categoryId + " n' ete trouve dans la BDD")
    );
        }
    	
    	
    	
    

    
	  public void updateCategorie(Integer id, Category Category) {
		    System.out.println("Update Categorie with ID = " + id + "...");
		 
		    Optional<Category> categoryInfo = categoryRepository.findById(id);
		 
		    if (categoryInfo.isPresent()) {
		    	Category categorie = categoryInfo.get();
		    	categorie.setCode(Category.getCode());
		          categorie.setDesignation(Category.getDesignation());
		           
		          Category cat = categoryRepository.save(categorie);
		         
		  }
	  }
	  
	  
	  public void generateEcel(HttpServletResponse response) throws IOException {
	        List<Category> categories = categoryRepository.findAll();
	        Workbook workbook = new HSSFWorkbook();
	        Sheet sheet = workbook.createSheet("cat info");
	        Row row = sheet.createRow(0);

	        row.createCell(0).setCellValue("ID");
	        row.createCell(1).setCellValue("code");
	        row.createCell(2).setCellValue("description");

	        int dataRowIndex = 1;
	        for (Category category : categories) {
	            Row dataRow = sheet.createRow(dataRowIndex);
	            dataRow.createCell(0).setCellValue(category.getId());
	            dataRow.createCell(1).setCellValue(category.getCode());
	            dataRow.createCell(2).setCellValue(category.getDesignation());
	            dataRowIndex++;
	        }

	        try (ServletOutputStream ops = response.getOutputStream()) {
	            workbook.write(ops);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            workbook.close();
	        }
	    }



	/*  public void saveCustomersToDatabase(MultipartFile file){
	        if(ExcelUploadService.isValidExcelFile(file)){
	            try {
	                List<Category> customers = ExcelUploadService.getCustomersDataFromExcel(file.getInputStream());
	                this.categoryRepository.saveAll(customers);
	            } catch (IOException e) {
	                throw new IllegalArgumentException("The file is not a valid excel file");
	            }
	        }
	    }*/
	  
}