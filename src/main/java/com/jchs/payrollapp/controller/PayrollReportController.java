package com.jchs.payrollapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.report.PayrollReport;
import com.jchs.payrollapp.model.report.PayrollReportItem;
import com.jchs.payrollapp.report.excel.PayrollReportExcelGenerator;
import com.jchs.payrollapp.service.EmployeeService;
import com.jchs.payrollapp.service.ReportService;
import com.jchs.payrollapp.util.DateUtil;
import com.jchs.payrollapp.util.ExcelUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class PayrollReportController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(PayrollReportController.class);
	
	@Autowired private EmployeeService employeeService;
	@Autowired private ReportService reportService;
	
	@FXML private ComboBox<Month> monthComboBox;
	@FXML private ComboBox<Integer> yearComboBox;
	@FXML private ComboBox<Employee> employeeComboBox;
	@FXML private AppTableView<PayrollReportItem> reportTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Payroll Report");
		monthComboBox.getItems().setAll(Month.values());
		yearComboBox.getItems().setAll(DateUtil.getYearDropdownValues());
		employeeComboBox.getItems().setAll(employeeService.getAllActiveEmployees());
		
		LocalDate previousMonth = LocalDate.now().minusMonths(1);
		monthComboBox.setValue(previousMonth.getMonth());
		yearComboBox.setValue(previousMonth.getYear());
	}

	@FXML 
	public void doOnBack() {
		stageController.showReportListScreen();
	}

	@FXML 
	public void generateReport() {
		PayrollReport report = reportService.generatePayrollReport(getYearMonthCriteria(), employeeComboBox.getValue());
		reportTable.setItemsThenFocus(report.getItems());
		if (report.getItems().isEmpty()) {
			ShowDialog.error("No records found");
		}
	}

	private YearMonth getYearMonthCriteria() {
		int month = monthComboBox.getValue().getValue();
		int year = yearComboBox.getValue();
		return YearMonth.of(year, month);
	}

	@FXML 
	public void generateExcelReport() {
		if (!validateFields()) {
			return;
		}
		
        FileChooser fileChooser = ExcelUtil.getSaveExcelFileChooser(getExcelFilename());
        File file = fileChooser.showSaveDialog(stageController.getStage());
        if (file == null) {
        	return;
        }
		
		try (
			Workbook workbook = new PayrollReportExcelGenerator().generate(
					reportService.generatePayrollReport(getYearMonthCriteria(), employeeComboBox.getValue()));
			FileOutputStream out = new FileOutputStream(file);
		) {
			workbook.write(out);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		if (ShowDialog.confirm("Excel file generated.\nDo you wish to open the file?")) {
			ExcelUtil.openExcelFile(file);
		}
	}

	private boolean validateFields() {
		if (isMonthNotSpecified()) {
			ShowDialog.error("Month must be specified");
			monthComboBox.requestFocus();
			return false;
		}
		
		if (isYearNotSpecified()) {
			ShowDialog.error("Year must be specified");
			yearComboBox.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isMonthNotSpecified() {
		return monthComboBox.getValue() == null;
	}

	private boolean isYearNotSpecified() {
		return yearComboBox.getValue() == null;
	}

	private String getExcelFilename() {
		YearMonth yearMonth = getYearMonthCriteria();
		return MessageFormat.format("payroll_report_{0}_{1}-MEY.xlsx", 
				String.valueOf(yearMonth.getYear()),
				StringUtils.leftPad(String.valueOf(yearMonth.getMonthValue()), 2, '0'));
	}

}
