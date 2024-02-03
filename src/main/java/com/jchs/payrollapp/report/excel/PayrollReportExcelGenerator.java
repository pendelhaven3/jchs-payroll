package com.jchs.payrollapp.report.excel;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jchs.payrollapp.model.report.PayrollReport;
import com.jchs.payrollapp.model.report.PayrollReportItem;

public class PayrollReportExcelGenerator {

	private static final int DATA_ROWS_START = 5;
	private static final int SUMMARY_ROWS_END = 16;
	
	public Workbook generate(PayrollReport report) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/excel/payroll_report.xlsx"));
		
		int month = report.getYearMonth().getMonthValue();
		int year = report.getYearMonth().getYear();
		
		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setDataFormat((short)4);
		
		Sheet sheet = workbook.getSheetAt(0);
		workbook.setSheetName(0, String.valueOf(month) + "-" + String.valueOf(year).substring(2));
		
		Cell cell = null;
		Row row = null;
		
		row = sheet.getRow(1);
		cell = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
		cell.setCellValue("REPORT_" + StringUtils.leftPad(String.valueOf(month), 2, '0') + "_" + String.valueOf(year));

		for (int i = 0; i < report.getItems().size(); i++) {
			PayrollReportItem item = report.getItems().get(i);
			
			if (i > 0) {
				sheet.shiftRows(DATA_ROWS_START + i, SUMMARY_ROWS_END + (i - 1), 1);
			}
			
			row = sheet.createRow(DATA_ROWS_START + i);
			
			cell = row.createCell(0);
			cell.setCellValue(item.getEmployee().toString());
			
			cell = row.createCell(1);
			cell.setCellValue(item.getEmployee().getTin());
			
			cell = row.createCell(2);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssReportItem().getMonthlyPay().doubleValue());
			
			cell = row.createCell(3);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssReportItem().getEmployeeContribution().doubleValue());
			
			cell = row.createCell(4);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssReportItem().getEmployerContribution().doubleValue());
			
			cell = row.createCell(5);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssReportItem().getTotalContribution().doubleValue());
			
			cell = row.createCell(6);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssReportItem().getEmployeeCompensation().doubleValue());
			
			cell = row.createCell(7);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssReportItem().getEmployeeProvidentFundContribution().doubleValue());
			
			cell = row.createCell(8);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getSssReportItem().getEmployerProvidentFundContribution().doubleValue());
			
			cell = row.createCell(9);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getPhilHealthReportItem().getDue().doubleValue());
			
			cell = row.createCell(10);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getPhilHealthReportItem().getDue().doubleValue());
			
			cell = row.createCell(11);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getPagIbigReportItem().getEmployeeContribution().doubleValue());
			
			cell = row.createCell(12);
			cell.setCellStyle(numberStyle);
			cell.setCellValue(item.getPagIbigReportItem().getEmployerContribution().doubleValue());
		}
		
		row = sheet.getRow(DATA_ROWS_START + report.getItems().size() + 1);
		
		cell = row.getCell(2);
        cell.setCellFormula(MessageFormat.format("SUM(C6:C{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(3);
        cell.setCellFormula(MessageFormat.format("SUM(D6:D{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(4);
        cell.setCellFormula(MessageFormat.format("SUM(E6:E{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(5);
        cell.setCellFormula(MessageFormat.format("SUM(F6:F{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(6);
        cell.setCellFormula(MessageFormat.format("SUM(G6:G{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(7);
        cell.setCellFormula(MessageFormat.format("SUM(H6:H{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(8);
        cell.setCellFormula(MessageFormat.format("SUM(I6:I{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(9);
        cell.setCellFormula(MessageFormat.format("SUM(J6:J{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(10);
        cell.setCellFormula(MessageFormat.format("SUM(K6:K{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(11);
        cell.setCellFormula(MessageFormat.format("SUM(L6:L{0})", DATA_ROWS_START + report.getItems().size()));
		
		cell = row.getCell(12);
        cell.setCellFormula(MessageFormat.format("SUM(M6:M{0})", DATA_ROWS_START + report.getItems().size()));
		
        XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		return workbook;
	}

}
