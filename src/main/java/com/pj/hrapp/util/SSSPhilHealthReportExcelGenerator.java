package com.pj.hrapp.util;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.model.report.SSSPhilHealthReportItem;

public class SSSPhilHealthReportExcelGenerator {

	public Workbook generate(SSSPhilHealthReport report) throws IOException {
		Workbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/excel/sss_philhealth.xlsx"));
		
		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setDataFormat((short)4);
		
		Sheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		Row row = null;
		SSSPhilHealthReportItem item = null;
		
		for (int i = 0; i < report.getItems().size(); i++) {
			item = report.getItems().get(i);
			row = sheet.getRow(1 + i);
			
			if (row == null) {
			    row = sheet.createRow(1 + i);
			}
			
			cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getEmployeeFullName());
			
            cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(item.getSssNumber());
			
            cell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(item.getPhilHealthNumber());
            
			cell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssEmployeeContribution().doubleValue());
			
			cell = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssEmployerContribution().doubleValue());
			
			cell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssTotalContribution().doubleValue());
			
			cell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getMonthlyPay().doubleValue());
			
			cell = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getPagibigContribution().doubleValue());
		}
		
		return workbook;
	}

}
