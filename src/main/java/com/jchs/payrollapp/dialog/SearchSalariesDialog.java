package com.jchs.payrollapp.dialog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.PaySchedule;
import com.jchs.payrollapp.model.search.SalarySearchCriteria;
import com.jchs.payrollapp.service.EmployeeService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

@Component
public class SearchSalariesDialog extends AbstractDialog {

	@Autowired private EmployeeService employeeService;
	
	@FXML private ComboBox<Employee> employeeComboBox;
	@FXML private ComboBox<PaySchedule> payScheduleComboBox;
	
	private SalarySearchCriteria searchCriteria;
	
	@Parameter private Employee employee;
	@Parameter private PaySchedule paySchedule;
	
	@Override
	protected String getDialogTitle() {
		return "Search Salaries";
	}

	@Override
	protected void updateDisplay() {
		searchCriteria = null;
		employeeComboBox.setItems(FXCollections.observableList(employeeService.getAllEmployees()));
		payScheduleComboBox.getItems().setAll(PaySchedule.SEMIMONTHLY);
		payScheduleComboBox.setValue(PaySchedule.SEMIMONTHLY);
		
		employeeComboBox.setValue(employee);
		payScheduleComboBox.setValue(paySchedule);
	}

	@Override
	protected String getSceneName() {
		return "searchSalariesDialog";
	}

	@FXML
	public void saveSearchCriteria() {
		searchCriteria = new SalarySearchCriteria();
		searchCriteria.setEmployee(employeeComboBox.getValue());
		searchCriteria.setPaySchedule(payScheduleComboBox.getValue());
		
		hide();
	}

	public SalarySearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

}
