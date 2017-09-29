package com.pj.hrapp.dialog;

import org.springframework.stereotype.Component;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.model.search.EmployeeSearchCriteria;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

@Component
public class SearchEmployeesDialog extends AbstractDialog {

	@FXML private TextField lastNameField;
	@FXML private TextField firstNameField;
	@FXML private ComboBox<String> statusComboBox;
	
	@Parameter private EmployeeSearchCriteria searchCriteria;
	
	@Override
	protected String getDialogTitle() {
		return "Search Employees";
	}

	@Override
	protected void updateDisplay() {
		statusComboBox.setItems(FXCollections.observableArrayList("All", "Active", "Resigned"));
		
		if (searchCriteria != null) {
			lastNameField.setText(searchCriteria.getLastName());
			firstNameField.setText(searchCriteria.getFirstName());
			
			if (searchCriteria.getResigned() != null) {
				statusComboBox.setValue(searchCriteria.getResigned() ? "Resigned" : "Active");
			} else {
				statusComboBox.setValue("All");
			}
			
		} else {
			statusComboBox.getSelectionModel().select(1);
		}
		
		searchCriteria = null;
	}

	@Override
	protected String getSceneName() {
		return "searchEmployeesDialog";
	}

	@FXML
	public void saveSearchCriteria() {
		searchCriteria = new EmployeeSearchCriteria();
		searchCriteria.setLastName(lastNameField.getText());
		searchCriteria.setFirstName(firstNameField.getText());
		searchCriteria.setResigned(getStatusComboBoxValue());
		hide();
	}

	private Boolean getStatusComboBoxValue() {
		if (statusComboBox.getSelectionModel().getSelectedIndex() == 0) {
			return null;
		} else {
			return "Resigned".equals(statusComboBox.getValue());
		}
	}

	public EmployeeSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

}
