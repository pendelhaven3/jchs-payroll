package com.jchs.payrollapp.util;

import java.io.IOException;
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
import com.jchs.payrollapp.model.report.PagIbigReport;
import com.jchs.payrollapp.model.report.PagIbigReportItem;

public class PagIbigReportExcelGenerator {

    private Row row;
    private Cell cell;
    private CellStyle numberStyle;
    private CellStyle numberBoldStyle;
    private CellStyle headerStyle;
    private CellStyle boldStyle;
    
    public Workbook generate(PagIbigReport report, CompanyProfile companyProfile) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        Sheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 256 * 30);
        sheet.setColumnWidth(1, 256 * 15);
        sheet.setColumnWidth(2, 256 * 12);
        sheet.setColumnWidth(3, 256 * 10);
        sheet.setColumnWidth(4, 256 * 10);
        sheet.setColumnWidth(5, 256 * 10);
        sheet.setColumnWidth(6, 256 * 8);
        
        createStyles(workbook);
        
        row = sheet.createRow(0);
        
        cell = row.createCell(0);
        cell.setCellValue(companyProfile.getName());
        
        nextRow();
        
        cell = row.createCell(0);
        cell.setCellValue(getReportCode(report));
        
        nextRow();
        
        cell = row.createCell(0);
        cell.setCellValue(companyProfile.getPagibigNumber());
        
        nextRow();
        nextRow();
        
        addHeaders();
        
        nextRow();
        
        addDataRows(report.getItems());
        
        nextRow();
        
        addTotalRow(report);
        
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
        cell.setCellValue("Pag-IBIG No.");
        
        cell = row.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("EE");
        
        cell = row.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("ER");
    }

    private void addDataRows(List<PagIbigReportItem> items) {
        PagIbigReportItem item = null;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            
            cell = row.createCell(0);
            cell.setCellValue(item.getEmployeeName());
            
            cell = row.createCell(1);
            cell.setCellValue(item.getPagIbigNumber());
            
            cell = row.createCell(2);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getEmployeeContribution().doubleValue());
            
            cell = row.createCell(3);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getEmployerContribution().doubleValue());
            
            nextRow();
        }
    }
    
    private void addTotalRow(PagIbigReport report) {
        cell = row.createCell(0);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Total");
        
        cell = row.createCell(2);
        cell.setCellStyle(numberBoldStyle);
        cell.setCellValue(report.getTotalEmployeeContribution().doubleValue());
        
        cell = row.createCell(3);
        cell.setCellStyle(numberBoldStyle);
        cell.setCellValue(report.getTotalEmployerContribution().doubleValue());
    }

    private void nextRow() {
        row = row.getSheet().createRow(row.getRowNum() + 1);
    }
    
	private String getReportCode(PagIbigReport report) {
		int month = report.getYearMonth().getMonthValue();
		int year = report.getYearMonth().getYear();
		
		return MessageFormat.format("PAGIBIG REPORT_{0}_{1}",
				StringUtils.leftPad(String.valueOf(month), 2, '0'),
				String.valueOf(year));
	}
    
}
