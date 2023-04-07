package com.jchs.payrollapp.dialog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeLoanType;
import com.jchs.payrollapp.model.search.EmployeeLoanSearchCriteria;
import com.jchs.payrollapp.service.EmployeeService;
import com.jchs.payrollapp.service.EmployeeLoanService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

@Component
public class SearchEmployeeLoansDialog extends AbstractDialog {

	@Autowired private EmployeeService employeeService;
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private ComboBox<Employee> employeeComboBox;
	@FXML private ComboBox<EmployeeLoanType> loanTypeComboBox;
	@FXML private ComboBox<String> paidComboBox;
	
	private EmployeeLoanSearchCriteria searchCriteria;
	
	@Override
	protected String getDialogTitle() {
		return "Search Employee Loans";
	}

	@Override
	protected void updateDisplay() {
		searchCriteria = null;
		employeeComboBox.setItems(FXCollections.observableList(employeeService.getAllEmployees()));
		loanTypeComboBox.getItems().addAll(employeeLoanService.getAllEmployeeLoanTypes());
		paidComboBox.setItems(FXCollections.observableArrayList("Paid", "Not Paid"));
	}

	@Override
	protected String getSceneName() {
		return "searchEmployeeLoansDialog";
	}

	@FXML
	public void saveSearchCriteria() {
		searchCriteria = new EmployeeLoanSearchCriteria();
		searchCriteria.setEmployee(employeeComboBox.getValue());
		searchCriteria.setPaid(getPaidComboBoxValue());
		searchCriteria.setEmployeeLoanType(loanTypeComboBox.getValue());
		hide();
	}

	private Boolean getPaidComboBoxValue() {
		if (paidComboBox.getValue() == null) {
			return null;
		} else {
			return "Paid".equals(paidComboBox.getValue());
		}
	}

	public EmployeeLoanSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

}
