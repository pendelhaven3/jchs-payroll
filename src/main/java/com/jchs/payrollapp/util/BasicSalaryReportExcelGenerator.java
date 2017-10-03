package com.jchs.payrollapp.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.jchs.payrollapp.model.report.BasicSalaryReport;
import com.jchs.payrollapp.model.report.BasicSalaryReportItem;

public class BasicSalaryReportExcelGenerator {

	public Workbook generate(BasicSalaryReport report) {
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(getClass().getResourceAsStream("/excel/basic salary.xlsx"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		Row row = null;
		
		cell = sheet.getRow(2).getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cell.setCellValue(report.getPeriodDescription());
		
		int i = 0;
		for (BasicSalaryReportItem item : report.getItems()) {
			row = sheet.getRow(8 + i);
			if (row == null) {
				row = sheet.createRow(8 + i);
			}
			
			cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getEmployeeName());
			
			cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellValue(item.getTotalSalary().doubleValue());
			
			i = i + 2;
		}
		
		row = sheet.createRow(8 + i);
		
		cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cell.setCellValue("Grand Total");
		
		cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cell.setCellValue(report.getTotal().doubleValue());
		
		return workbook;
	}
	
}
