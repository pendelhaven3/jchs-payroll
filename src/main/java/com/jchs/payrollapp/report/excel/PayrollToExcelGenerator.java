package com.jchs.payrollapp.report.excel;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.PayslipAdjustment;
import com.jchs.payrollapp.model.PayslipBasicPayItem;
import com.jchs.payrollapp.service.PayrollService;

@Component
public class PayrollToExcelGenerator {

	private static final int PAYSLIPS_PER_SHEET = 9;
    private static final int ADJUSTMENT_LINES_PER_PAYSLIP = 11;

	@Autowired private PayrollService payrollService;
	
	private int[] payslipRows = new int[] {0, 0, 0, 17, 17, 17, 34, 34, 34};
	
	private int[][] payslipColumns = new int[][] {
		{0, 1, 2},
		{3, 4, 5},
		{6, 7, 8},
		{0, 1, 2},
		{3, 4, 5},
		{6, 7, 8},
		{0, 1, 2},
		{3, 4, 5},
		{6, 7, 8}
	};
	
	private String[][] cellNames;
	
	public PayrollToExcelGenerator() {
		generateCellNames();
	}
	
	private void generateCellNames() {
		int rows = 50;
		int columns = 8;
		String[] columnNames = new String[] {"A", "B", "C", "D", "E", "F", "G", "H"};
		
		cellNames = new String[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cellNames[i][j] = columnNames[j] + String.valueOf(i);
			}
		}
	}
	
	public XSSFWorkbook generate(Payroll payroll) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(getClass().getResourceAsStream("/excel/payslip.xlsx"));
		CellStyle leftBorderCellStyle = createCellStyleWithLeftBorder(workbook);
		CellStyle rightBorderedAmountCellStyle = createAmountCellStyleWithRightBorder(workbook);
        CellStyle totalCellStyle = createTotalCellStyle(workbook);
		
		Sheet sheet = null;
		Cell cell = null;
		Row row = null;
		
		int sheetNumber = 0;
		sheet = workbook.getSheetAt(sheetNumber);
		
		int i = 0; // section number (0-8 only)
		for (Payslip payslip : payroll.getPayslips()) {
		    if (i == PAYSLIPS_PER_SHEET) {
		        sheetNumber++;
		        sheet = workbook.getSheetAt(sheetNumber);
		        i = 0;
		    }
		    
		    payslip = payrollService.getPayslip(payslip.getId());
		    
            int currentRow = payslipRows[i];
            
            cell = sheet.getRow(currentRow).getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(payslip.getEmployee().getFirstAndLastName());
            
            currentRow++;
            
            cell = sheet.getRow(currentRow).getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(payslip.getPayroll().getPayDate());
            
            currentRow += 2;

            List<PayslipBasicPayItem> items = payslip.getBasicPayItems();
            for (int j = 0; j < items.size(); j++) {
                PayslipBasicPayItem item = items.get(j);
                row = sheet.getRow(currentRow);
                
                switch (payslip.getPayType()) {
                case PER_DAY:
                    cell = row.getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(item.getRate().doubleValue());
                    cell.setCellStyle(leftBorderCellStyle);
                    
                    cell = row.getCell(payslipColumns[i][1], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(item.getNumberOfDays());
                    
                    cell = row.getCell(payslipColumns[i][2], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(new StringBuilder()
                            .append(cellNames[row.getRowNum() + 1][payslipColumns[i][0]])
                            .append("*")
                            .append(cellNames[row.getRowNum() + 1][payslipColumns[i][1]])
                            .toString());
                    break;
                case FIXED_RATE:
                    cell = row.getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellValue("@");

                    cell = row.getCell(payslipColumns[i][1], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(item.getNumberOfDays());
                    
                    cell = row.getCell(payslipColumns[i][2], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellValue(item.getAmount().doubleValue());
                    break;
                }
                
                currentRow++;
            }
            
            int adjustmentLines = 0;
            
            List<EmployeeLoanPayment> loanPayments = payslip.getLoanPayments();
            for (int j = 0; j < loanPayments.size(); j++) {
                EmployeeLoanPayment loanPayment = loanPayments.get(j);
                row = sheet.getRow(currentRow);
                
                cell = row.getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(loanPayment.getDescription());
                
                cell = row.getCell(payslipColumns[i][2], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(loanPayment.getAmount().negate().doubleValue());
                cell.setCellStyle(rightBorderedAmountCellStyle);
                
                currentRow++;
                adjustmentLines++;
            }
            
            List<PayslipAdjustment> adjustments = payslip.getAdjustments();
            for (int j = 0; j < adjustments.size(); j++) {
                if (adjustmentLines >= ADJUSTMENT_LINES_PER_PAYSLIP) {
                    i++;
                    
                    if (i == PAYSLIPS_PER_SHEET) {
                        sheetNumber++;
                        sheet = workbook.getSheetAt(sheetNumber);
                        i = 0;
                    }
                    
                    currentRow = payslipRows[i];
                    adjustmentLines = 0;
                    
                    cell = sheet.getRow(currentRow).getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellValue(payslip.getEmployee().getFirstAndLastName());
                    
                    currentRow++;
                    
                    cell = sheet.getRow(currentRow).getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellValue(payslip.getPayroll().getPayDate());
                    
                    currentRow += 3;
                }
                
                PayslipAdjustment adjustment = adjustments.get(j);
                row = sheet.getRow(currentRow);
                
                cell = row.getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(adjustment.getDescription());
                
                cell = row.getCell(payslipColumns[i][2], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(adjustment.getAmount().doubleValue());
                cell.setCellStyle(rightBorderedAmountCellStyle);
                
                currentRow++;
                adjustmentLines++;
            }
            
            // TOTAL
            row = sheet.getRow(payslipRows[i] + 15);
            
            cell = row.getCell(payslipColumns[i][0], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue("TOTAL");
            
            cell = row.getCell(payslipColumns[i][2], Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(payslip.getNetPay().doubleValue());
            cell.setCellStyle(totalCellStyle);
            
            i++;
		}
		
        XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		return workbook;
	}

    private CellStyle createCellStyleWithLeftBorder(XSSFWorkbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setBorderLeft(BorderStyle.THIN);
		return cellStyle;
	}
	
	private CellStyle createAmountCellStyleWithRightBorder(XSSFWorkbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat((short)BuiltinFormats.getBuiltinFormat("#,##0.00_);(#,##0.00)"));
		cellStyle.setBorderRight(BorderStyle.THIN);
		return cellStyle;
	}
	
    private CellStyle createTotalCellStyle(XSSFWorkbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat((short)BuiltinFormats.getBuiltinFormat("#,##0.00_);(#,##0.00)"));
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontName("Arial");
        boldFont.setFontHeightInPoints((short)10);
        cellStyle.setFont(boldFont);
        
        return cellStyle;
    }
    
}
