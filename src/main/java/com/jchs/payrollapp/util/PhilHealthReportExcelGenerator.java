package com.jchs.payrollapp.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jchs.payrollapp.model.CompanyProfile;
import com.jchs.payrollapp.model.report.PhilHealthReport;
import com.jchs.payrollapp.model.report.PhilHealthReportItem;

public class PhilHealthReportExcelGenerator {

    private Row row;
    private Cell cell;
    private CellStyle numberStyle;
    private CellStyle numberBoldStyle;
    private CellStyle headerStyle;
    private CellStyle boldStyle;
    
    public Workbook generate(PhilHealthReport report, CompanyProfile companyProfile) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        Sheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 256 * 30);
        sheet.setColumnWidth(1, 256 * 15);
        sheet.setColumnWidth(2, 256 * 12);
        sheet.setColumnWidth(3, 256 * 10);
        
        createStyles(workbook);
        
        row = sheet.createRow(0);
        
        cell = row.createCell(0);
        cell.setCellValue(companyProfile.getName());
        
        nextRow();
        
        cell = row.createCell(0);
        cell.setCellValue(getReportCode(report));
        
        nextRow();
        
        cell = row.createCell(0);
        cell.setCellValue(companyProfile.getPhilhealthNumber());
        
        nextRow();
        nextRow();
        
        addHeaders();
        
        nextRow();
        
        addDataRows(report.getItems());
        
        nextRow();
        nextRow();
        
        cell = row.createCell(0);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Grand Total");
        
        cell = row.createCell(3);
        cell.setCellStyle(numberBoldStyle);
        cell.setCellValue(report.getTotalDue().doubleValue());
        
        cell = row.createCell(4);
        cell.setCellStyle(numberBoldStyle);
        cell.setCellValue(report.getTotalDue().doubleValue());
        
        cell = row.createCell(5);
        cell.setCellStyle(numberBoldStyle);
        cell.setCellValue(report.getTotalDue().multiply(BigDecimal.valueOf(2L)).doubleValue());
        
        return workbook;
    }
    
    private void createStyles(Workbook workbook) {
        numberStyle = workbook.createCellStyle();
        numberStyle.setDataFormat((short)4);
        
        headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        
        numberBoldStyle = workbook.createCellStyle();
        numberBoldStyle.setFont(boldFont);
        numberBoldStyle.setDataFormat((short)4);
        
        boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
    }

    private void addHeaders() {
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Employee");
        
        cell = row.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("PhilHealth No.");
        
        cell = row.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Monthly Pay");
        
        cell = row.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("EE");
        
        cell = row.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ER");
        
        cell = row.createCell(5);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Total");
    }

    private void addDataRows(List<PhilHealthReportItem> items) {
        PhilHealthReportItem item = null;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            
            cell = row.createCell(0);
            cell.setCellValue(item.getEmployeeName());
            
            cell = row.createCell(1);
            cell.setCellValue(item.getPhilHealthNumber());
            
            cell = row.createCell(2);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getMonthlyPay().doubleValue());
            
            cell = row.createCell(3);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getDue().doubleValue());
            
            cell = row.createCell(4);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getDue().doubleValue());
            
            cell = row.createCell(5);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getDue().multiply(BigDecimal.valueOf(2L)).doubleValue());
            
            nextRow();
        }
    }
    
    private void nextRow() {
        row = row.getSheet().createRow(row.getRowNum() + 1);
    }
    
	private String getReportCode(PhilHealthReport report) {
		int month = report.getYearMonth().getMonthValue();
		int year = report.getYearMonth().getYear();
		
		return MessageFormat.format("PHILHEALTH REPORT_{0}_{1}",
				StringUtils.leftPad(String.valueOf(month), 2, '0'),
				String.valueOf(year));
	}
    
}
