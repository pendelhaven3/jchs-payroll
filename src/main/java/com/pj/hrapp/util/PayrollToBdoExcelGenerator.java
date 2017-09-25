package com.pj.hrapp.util;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.PayrollService;

@Component
public class PayrollToBdoExcelGenerator {

	@Autowired private PayrollService payrollService;
	
	public Workbook generate(Payroll payroll) throws IOException {
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(getClass().getResourceAsStream("/excel/BDO EPCI Regular Payroll.xlsm"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		Sheet sheet = workbook.getSheetAt(0);
		Cell cell = null;
		Row row = null;
		
		cell = sheet.getRow(1).getCell(1);
		cell.setCellValue(payroll.getPayDate());
		
		for (int i = 0; i < payroll.getPayslips().size(); i++) {
			Payslip payslip = payrollService.getPayslip(payroll.getPayslips().get(i).getId());
			
			row = sheet.getRow(7 + i);
			
			cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellValue(payslip.getEmployee().getAtmAccountNumber());
			
			cell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellValue(payslip.getAtmPay().doubleValue());
			
			cell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellValue(payslip.getEmployee().getFullName());
			
			cell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			cell.setCellValue("Ok");
		}
		
		return workbook;
	}

}
