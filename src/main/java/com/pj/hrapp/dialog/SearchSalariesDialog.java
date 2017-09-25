package com.pj.hrapp.dialog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PaySchedule;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.service.EmployeeService;

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
		payScheduleComboBox.setItems(FXCollections.observableArrayList(PaySchedule.values()));
		
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
