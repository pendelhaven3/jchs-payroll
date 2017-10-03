package com.jchs.payrollapp.util;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.PayslipAdjustment;
import com.jchs.payrollapp.model.PayslipBasicPayItem;
import com.jchs.payrollapp.service.PayrollService;

@Component
public class PayrollToExcelGenerator {

	private static final int PAYSLIPS_PER_SHEET = 9;

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
		
		Sheet sheet = null;
		Cell cell = null;
		Row row = null;
		
		List<List<Payslip>> payslipGroups = Lists.partition(payroll.getPayslips(), PAYSLIPS_PER_SHEET);
		for (int s = 0; s < payslipGroups.size(); s++) {
			sheet = workbook.getSheetAt(s);
			List<Payslip> payslips = payslipGroups.get(s);
			
			for (int i = 0; i < payslips.size(); i++) {
				Payslip payslip = payrollService.getPayslip(payslips.get(i).getId());
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
					
					switch (payslip.getEmployee().getPayType()) {
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
				}
				
				List<PayslipAdjustment> adjustments = payslip.getAdjustments();
				for (int j = 0; j < adjustments.size(); j++) {
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
				}
			}
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
	
}
