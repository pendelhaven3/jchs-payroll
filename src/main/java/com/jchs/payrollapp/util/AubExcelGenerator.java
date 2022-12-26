package com.jchs.payrollapp.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.Payslip;

public class AubExcelGenerator {

	public HSSFWorkbook generate(Payroll payroll) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook(getClass().getResourceAsStream("/excel/netpay_upload.xls"));
		CellStyle rightBorderedAmountCellStyle = createAmountCellStyle(workbook);
		
		List<Payslip> payslips = payroll.getPayslips();
		payslips.sort((o1, o2) -> o1.getEmployee().getEmployeeNumber().compareTo(o2.getEmployee().getEmployeeNumber()));
		
		Sheet sheet = workbook.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		
		int currentRow = 3;
		int i = 0;
		for (Payslip payslip : payslips) {
			Employee employee = payslip.getEmployee();
			row = sheet.createRow(currentRow + i);
			
			cell = row.createCell(0, CellType.STRING);
			cell.setCellValue(StringUtils.leftPad(employee.getEmployeeNumber().toString(), 3, '0'));
			
			cell = row.createCell(1, CellType.STRING);
			cell.setCellValue(employee.getFullNameWithMiddleName());
			
			cell = row.createCell(2, CellType.STRING);
			cell.setCellValue(employee.getAccountNumber());
			
			cell = row.createCell(3, CellType.STRING);
			cell.setCellValue(employee.getBankCode());
			
			cell = row.createCell(4, CellType.NUMERIC);
			cell.setCellStyle(rightBorderedAmountCellStyle);
			cell.setCellValue(payslip.getNetPay().doubleValue());
			
			i++;
		}
		
		return workbook;
	}

	private CellStyle createAmountCellStyle(HSSFWorkbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat((short)BuiltinFormats.getBuiltinFormat("#,##0.00_);(#,##0.00)"));
		return cellStyle;
	}
	
}
