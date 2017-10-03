package com.jchs.payrollapp.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.dialog.SearchSalariesDialog;
import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.DoubleClickEventHandler;
import com.jchs.payrollapp.gui.component.EnterKeyEventHandler;
import com.jchs.payrollapp.model.Salary;
import com.jchs.payrollapp.model.search.SalarySearchCriteria;
import com.jchs.payrollapp.service.SalaryService;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalaryListController extends AbstractController {

	@Autowired private SalaryService salaryService;
	@Autowired private SearchSalariesDialog searchSalariesDialog;
	
	@FXML private AppTableView<Salary> salariesTable;
	
	@Parameter private SalarySearchCriteria searchCriteria;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Salary List");
		
		if (searchCriteria != null) {
			salariesTable.setItemsThenFocus(salaryService.searchSalaries(searchCriteria));
		} else {
			salariesTable.setItemsThenFocus(salaryService.getAllCurrentSalaries());
		}
		
		salariesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				updateSelectedSalary();
			}
		});
		salariesTable.setOnKeyPressed(new EnterKeyEventHandler() {
			
			@Override
			protected void onEnterKey() {
				updateSelectedSalary();
			}
		});
	}

	private void updateSelectedSalary() {
		if (!salariesTable.getSelectionModel().isEmpty()) {
			stageController.updateSalaryListScreenHistoryItemModel(
					Collections.singletonMap("searchCriteria", searchCriteria));
			stageController.showUpdateSalaryScreen(
					salariesTable.getSelectionModel().getSelectedItem());
		}
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML public void addSalary() {
		stageController.showAddSalaryScreen();
	}

	@FXML 
	public void searchSalaries() {
		searchSalariesDialog.showAndWait(getCurrentSearchCriteriaAsMap());
		
		SalarySearchCriteria criteria = searchSalariesDialog.getSearchCriteria();
		if (criteria != null) {
			salariesTable.setItemsThenFocus(salaryService.searchSalaries(criteria));
			searchCriteria = criteria;
		}
	}

	private Map<String, Object> getCurrentSearchCriteriaAsMap() {
		return searchCriteria != null ? searchCriteria.toMap() : null;
	}

}
