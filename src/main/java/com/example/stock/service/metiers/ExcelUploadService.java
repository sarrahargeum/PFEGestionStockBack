package com.example.stock.service.metiers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.example.stock.model.Category;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelUploadService {
	 private static final Logger logger = Logger.getLogger(ExcelUploadService.class.getName());

	    public static boolean isValidExcelFile(MultipartFile file) {
	        return Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    }

	    public static List<Category> getCustomersDataFromExcel(InputStream inputStream) {
	        List<Category> customers = new ArrayList<>();
	        try {
	            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
	            XSSFSheet sheet = workbook.getSheet("customers");

	            if (sheet == null) {
	                logger.log(Level.SEVERE, "Sheet 'customers' not found in the Excel file.");
	                throw new NullPointerException("Sheet 'customers' is null.");
	            }

	            int rowIndex = 0;
	            for (Row row : sheet) {
	                if (rowIndex == 0) {
	                    rowIndex++;
	                    continue;
	                }

	                Iterator<Cell> cellIterator = row.iterator();
	                int cellIndex = 0;
	                Category customer = new Category();
	                while (cellIterator.hasNext()) {
	                    Cell cell = cellIterator.next();
	                    switch (cellIndex) {
	                        case 0 -> customer.setId((int) cell.getNumericCellValue());
	                        case 1 -> customer.setCode(cell.getStringCellValue());
	                        case 2 -> customer.setDesignation(cell.getStringCellValue());
	                        default -> {
	                        }
	                    }
	                    cellIndex++;
	                }
	                customers.add(customer);
	            }
	            workbook.close();
	        } catch (IOException e) {
	            logger.log(Level.SEVERE, "Error reading the Excel file: ", e);
	        } catch (NullPointerException e) {
	            logger.log(Level.SEVERE, "Sheet is null: ", e);
	        }
	        return customers;
	    }
	}
