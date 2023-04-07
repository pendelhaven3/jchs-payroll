package com.jchs.payrollapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.model.EmployeeLoanType;
import com.jchs.payrollapp.service.EmployeeLoanService;

import javafx.fxml.FXML;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeLoanTypeListController extends AbstractController {

	@Autowired
	private EmployeeLoanService employeeLoanService;
	
	@FXML private AppTableView<EmployeeLoanType> employeeLoanTypesTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Loan Type List");
		
		employeeLoanTypesTable.setItemsThenFocus(employeeLoanService.getAllEmployeeLoanTypes());
		employeeLoanTypesTable.setDoubleClickAndEnterKeyAction(() -> updateSelectedEmployeeLoanType());
	}

	private void updateSelectedEmployeeLoanType() {
		if (employeeLoanTypesTable.hasSelectedItem()) {
			stageController.showUpdateEmployeeLoanTypeScreen(employeeLoanTypesTable.getSelectedItem());
		}
	}

	@FXML
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML 
	public void addEmployeeLoanType() {
		stageController.showAddEmployeeLoanTypeScreen();
	}

}
