package com.jchs.payrollapp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.gui.component.AppDatePicker;
import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.EmployeeLoanType;
import com.jchs.payrollapp.service.ReportService;
import com.jchs.payrollapp.util.DateUtil;
import com.jchs.payrollapp.service.EmployeeLoanService;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class EmployeeLoanPaymentsReportController extends AbstractController {

	@Autowired private ReportService reportService;
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private AppDatePicker fromDateDatePicker;
	@FXML private AppDatePicker toDateDatePicker;
	@FXML private ComboBox<EmployeeLoanType> loanTypeComboBox;
	@FXML private AppTableView<EmployeeLoanPayment> reportTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Loan Payments Report");
		loanTypeComboBox.getItems().addAll(employeeLoanService.getAllEmployeeLoanTypes());
	}

	@FXML 
	public void doOnBack() {
		stageController.showReportListScreen();
	}

	@FXML 
	public void generateReport() {
		Date from = DateUtil.toDate(fromDateDatePicker.getValue());
		Date to = DateUtil.toDate(toDateDatePicker.getValue());
		EmployeeLoanType loanType = loanTypeComboBox.getValue();
		
		if (from == null || to == null) {
			ShowDialog.error("From Date and To Date must be specified");
			return;
		}
		
		List<EmployeeLoanPayment> payments = reportService.generateEmployeeLoanPaymentsReport(from, to, loanType);
		reportTable.setItemsThenFocus(payments);
		if (payments.isEmpty()) {
			ShowDialog.error("No records found");
		}
	}

}
