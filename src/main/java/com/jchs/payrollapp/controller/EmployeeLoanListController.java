package com.jchs.payrollapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.dialog.SearchEmployeeLoansDialog;
import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.model.EmployeeLoan;
import com.jchs.payrollapp.model.search.EmployeeLoanSearchCriteria;
import com.jchs.payrollapp.service.EmployeeLoanService;

import javafx.fxml.FXML;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeLoanListController extends AbstractController {

	@Autowired private EmployeeLoanService employeeLoanService;
	@Autowired private SearchEmployeeLoansDialog searchEmployeeLoansDialog;
	
	@FXML private AppTableView<EmployeeLoan> employeeLoansTable;
	
	@Parameter private EmployeeLoanSearchCriteria searchCriteria;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Loan List");
		
		employeeLoansTable.setItemsThenFocus(employeeLoanService.findAllUnpaidEmployeeLoans());
		employeeLoansTable.setDoubleClickAndEnterKeyAction(() -> updateSelectedEmployeeLoan());
		
		if (searchCriteria != null) {
			searchEmployeeLoans(searchCriteria);
		}
	}

	private void updateSelectedEmployeeLoan() {
		if (employeeLoansTable.hasSelectedItem()) {
			stageController.showEmployeeLoanScreen(employeeLoansTable.getSelectedItem());
		}
	}

	@FXML
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML 
	public void addEmployeeLoan() {
		stageController.showAddEmployeeLoanScreen();
	}

	@FXML 
	public void searchEmployeeLoans() {
		searchEmployeeLoansDialog.showAndWait();
		
		EmployeeLoanSearchCriteria criteria = searchEmployeeLoansDialog.getSearchCriteria();
		if (criteria != null) {
			searchEmployeeLoans(criteria);
			this.searchCriteria = criteria;
		}
	}

	private void searchEmployeeLoans(EmployeeLoanSearchCriteria criteria) {
		List<EmployeeLoan> loans = employeeLoanService.searchEmployeeLoans(criteria);
		employeeLoansTable.setItemsThenFocus(loans);
		stageController.addCurrentScreenParameter("searchCriteria", criteria);
	}
	
}
