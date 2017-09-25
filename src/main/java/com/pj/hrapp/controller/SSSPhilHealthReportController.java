package com.pj.hrapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Month;
import java.time.YearMonth;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.report.SSSPhilHealthReport;
import com.pj.hrapp.model.report.SSSPhilHealthReportItem;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.ExcelUtil;
import com.pj.hrapp.util.SSSPhilHealthReportExcelGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SSSPhilHealthReportController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(SSSPhilHealthReportController.class);
	
	@Autowired private ReportService reportService;
	
	@FXML private ComboBox<Month> monthComboBox;
	@FXML private ComboBox<Integer> yearComboBox;
	@FXML private AppTableView<SSSPhilHealthReportItem> reportTable;
	
	private SSSPhilHealthReportExcelGenerator excelGenerator = new SSSPhilHealthReportExcelGenerator();
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("SSS/PhilHealth Report");
		monthComboBox.getItems().setAll(Month.values());
		yearComboBox.getItems().setAll(DateUtil.getYearDropdownValues());
	}

	@FXML 
	public void doOnBack() {
		stageController.showReportListScreen();
	}

	@FXML 
	public void generateReport() {
		SSSPhilHealthReport report = reportService.generateSSSPhilHealthReport(getYearMonthCriteria());
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
			Workbook workbook = excelGenerator.generate(
					reportService.generateSSSPhilHealthReport(getYearMonthCriteria()));
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
		return MessageFormat.format("sss_philhealth_{0}{1}.xlsx", 
				String.valueOf(yearMonth.getYear()), yearMonth.getMonth().getValue());
	}

}
