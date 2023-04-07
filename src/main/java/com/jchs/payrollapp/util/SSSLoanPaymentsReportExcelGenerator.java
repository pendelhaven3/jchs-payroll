package com.jchs.payrollapp.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.YearMonth;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.EmployeeLoanType;

public class SSSLoanPaymentsReportExcelGenerator {

    private Row row;
    private Cell cell;
    private CellStyle numberStyle;
    private CellStyle numberBoldStyle;
    private CellStyle headerStyle;
    private CellStyle boldStyle;
    private CellStyle dateStyle;
    
    public Workbook generate(List<EmployeeLoanPayment> items, EmployeeLoanType loanType, YearMonth yearMonth) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        Sheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, 256 * 30);
        sheet.setColumnWidth(1, 256 * 15);
        sheet.setColumnWidth(2, 256 * 15);
        sheet.setColumnWidth(3, 256 * 15);
        sheet.setColumnWidth(4, 256 * 19);
        
        createStyles(workbook);
        
        row = sheet.createRow(0);
        
        addReportCodeRow(loanType, yearMonth);
        
        nextRow();
        nextRow();
        
        addHeaders();
        
        nextRow();
        
        addDataRows(items);
        
        nextRow();
        
        addTotalRow(items);
        
        return workbook;
    }
    
    private void addReportCodeRow(EmployeeLoanType loanType, YearMonth yearMonth) {
        cell = row.createCell(0);
        cell.setCellValue(getReportCode(loanType, yearMonth));
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
        
        dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("mm/dd/yyyy"));
    }

    private void addHeaders() {
        cell = row.createCell(0);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Employee");
        
        cell = row.createCell(1);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("SSS No.");
        
        cell = row.createCell(2);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Loan Date");
        
        cell = row.createCell(3);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Amount of Loan");
        
        cell = row.createCell(4);
        cell.setCellStyle(headerStyle);
        cell.setCellValue("Monthly Amortization");
    }

    private void addDataRows(List<EmployeeLoanPayment> items) {
        for (EmployeeLoanPayment item : items) {
            cell = row.createCell(0);
            cell.setCellValue(item.getLoan().getEmployee().getFullName());
            
            cell = row.createCell(1);
            cell.setCellValue(item.getLoan().getEmployee().getSssNumber());
            
            cell = row.createCell(2);
            cell.setCellStyle(dateStyle);
            cell.setCellValue(item.getLoan().getLoanDate());
            
            cell = row.createCell(3);
            cell.setCellStyle(numberStyle);
            // TODO:
//            cell.setCellValue(item.getLoan().getLoanAmount().doubleValue()); 
            
            cell = row.createCell(4);
            cell.setCellStyle(numberStyle);
            cell.setCellValue(item.getLoan().getPaymentAmount().doubleValue());
            
            nextRow();
        }
    }
    
    private void nextRow() {
        row = row.getSheet().createRow(row.getRowNum() + 1);
    }
    
    private void addTotalRow(List<EmployeeLoanPayment> items) {
        cell = row.createCell(0);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Total");
        
        cell = row.createCell(4);
        cell.setCellStyle(numberBoldStyle);
        cell.setCellValue(items.stream().map(item -> item.getAmount()).reduce(BigDecimal.ZERO, (x,y) -> x.add(y)).doubleValue());
    }
    
	private String getReportCode(EmployeeLoanType loanType, YearMonth yearMonth) {
		int month = yearMonth.getMonthValue();
		int year = yearMonth.getYear();
		
		return "";
		// TODO:
//		return MessageFormat.format("{0} PAYMENTS REPORT_{1}_{2}",
//				loanType.getDescription().toUpperCase(),
//				StringUtils.leftPad(String.valueOf(month), 2, '0'),
//				String.valueOf(year));
	}
    
}
