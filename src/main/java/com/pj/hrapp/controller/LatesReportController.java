package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppDatePicker;
import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.LatesReportItem;
import com.pj.hrapp.service.ReportService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

@Controller
@Scope("prototype")
public class LatesReportController extends AbstractController {

	@Autowired private ReportService reportService;
	
	@FXML private AppDatePicker dateFromDatePicker;
	@FXML private AppDatePicker dateToDatePicker;
	@FXML private AppTableView<LatesReportItem> latesTable;
	@FXML Text totalLatesText;
	
	@Override
	public void updateDisplay() {
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML 
	public void searchLates() {
		if (!validateFields()) {
			return;
		}
		LatesReport report = reportService.generateLatesReport(
				DateUtil.toDate(dateFromDatePicker.getValue()), 
				DateUtil.toDate(dateToDatePicker.getValue()));
		latesTable.setItems(report.getItems());
		totalLatesText.setText(FormatterUtil.formatAmount(report.getTotalLates()));
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
	
}
