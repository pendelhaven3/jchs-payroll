package com.jchs.payrollapp.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.dialog.SearchEmployeesDialog;
import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.DoubleClickEventHandler;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.search.EmployeeSearchCriteria;
import com.jchs.payrollapp.service.EmployeeService;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeListController extends AbstractController {

	@Autowired private EmployeeService employeeService;
	@Autowired private SearchEmployeesDialog searchEmployeesDialog;
	
	@FXML private AppTableView<Employee> employeesTable;
	
	@Parameter private EmployeeSearchCriteria searchCriteria;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee List");
		
		if (searchCriteria != null) {
			employeesTable.setItemsThenFocus(employeeService.searchEmployees(searchCriteria));
		} else {
			employeesTable.setItemsThenFocus(employeeService.getAllActiveEmployees());
		}
		
		employeesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				updateSelectedEmployee();
			}
		});
		
		employeesTable.setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				updateSelectedEmployee();
			}
		});
	}

	private void updateSelectedEmployee() {
		if (!employeesTable.getSelectionModel().isEmpty()) {
			stageController.showUpdateEmployeeScreen(
					employeesTable.getSelectionModel().getSelectedItem(), searchCriteria);
		}
	}

	@FXML
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addEmployee() {
		stageController.showAddEmployeeScreen();
	}
	
	@FXML
	public void searchEmployees() {
		searchEmployeesDialog.showAndWait(Collections.singletonMap("searchCriteria", searchCriteria));
		
		searchCriteria = searchEmployeesDialog.getSearchCriteria();
		if (searchCriteria != null) {
			List<Employee> employees = employeeService.searchEmployees(searchCriteria);
			employeesTable.setItemsThenFocus(employees);
		}
	}
	
}
