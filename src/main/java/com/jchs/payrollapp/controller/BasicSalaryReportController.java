package com.jchs.payrollapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.gui.component.AppDatePicker;
import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.report.BasicSalaryReport;
import com.jchs.payrollapp.model.report.BasicSalaryReportItem;
import com.jchs.payrollapp.service.ReportService;
import com.jchs.payrollapp.util.BasicSalaryReportExcelGenerator;
import com.jchs.payrollapp.util.DateUtil;
import com.jchs.payrollapp.util.ExcelUtil;
import com.jchs.payrollapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Controller
@Scope("prototype")
public class BasicSalaryReportController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(BasicSalaryReportController.class);
	
	@Autowired private ReportService reportService;
	
	@FXML private AppDatePicker dateFromDatePicker;
	@FXML private AppDatePicker dateToDatePicker;
	@FXML private AppTableView<BasicSalaryReportItem> itemsTable;
	@FXML Text totalText;
	
	private BasicSalaryReportExcelGenerator excelGenerator = new BasicSalaryReportExcelGenerator();
	
	@Override
	public void updateDisplay() {
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML 
	public void searchSalaries() {
		if (!validateFields()) {
			return;
		}
		
		BasicSalaryReport report = generateBasicSalaryReport();
		itemsTable.setItems(report.getItems());
		totalText.setText(FormatterUtil.formatAmount(report.getTotal()));
	}

	private BasicSalaryReport generateBasicSalaryReport() {
		return reportService.generateBasicSalaryReport(
				DateUtil.toDate(dateFromDatePicker.getValue()), 
				DateUtil.toDate(dateToDatePicker.getValue()));
	}

	private boolean validateFields() {
		if (!isDateFromSpecified()) {
			ShowDialog.error("Date From must be specified");
			dateFromDatePicker.requestFocus();
			return false;
		}
		if (!isDateToSpecified()) {
			ShowDialog.error("Date To must be specified");
			dateToDatePicker.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isDateFromSpecified() {
		return dateFromDatePicker.getValue() != null;
	}

	private boolean isDateToSpecified() {
		return dateToDatePicker.getValue() != null;
	}
	
	@FXML
	public void generateExcel() {
		BasicSalaryReport report = generateBasicSalaryReport();
		
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel files", "*.xlsx"));
        fileChooser.setInitialFileName(getExcelFilename(report));
        File file = fileChooser.showSaveDialog(stageController.getStage());
        if (file == null) {
        	return;
        }
		
		try (
			Workbook workbook = excelGenerator.generate(report);
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

	private String getExcelFilename(BasicSalaryReport report) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMyyyy");
		
		return new StringBuilder("basic salary report ")
				.append(dateFormat.format(DateUtil.toDate(report.getYearMonthFrom())))
				.append(" - ")
				.append(dateFormat.format(DateUtil.toDate(report.getYearMonthTo())))
				.append(".xlsx")
				.toString();
	}
	
}
