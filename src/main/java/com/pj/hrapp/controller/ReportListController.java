package com.pj.hrapp.controller;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReportListController extends AbstractController {

	@Override
	public void updateDisplay() {
		stageController.setTitle("Report List");
	}

	@FXML 
	public void goToSSSPhilHealthReportScreen() {
		stageController.showSSSPhilHealthReportScreen();
	}

	@FXML 
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML 
	public void goToLatesReportScreen() {
		stageController.showLatesReportScreen();
	}

	@FXML 
	public void goToBasicSalaryReportScreen() {
		stageController.showBasicSalaryReportScreen();
	}

	@FXML 
	public void goToEmployeeLoanPaymentsReportScreen() {
		stageController.showEmployeeLoanPaymentsReportScreen();
	}
	
}
