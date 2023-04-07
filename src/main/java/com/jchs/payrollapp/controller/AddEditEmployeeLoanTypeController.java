package com.jchs.payrollapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.exception.EmployeeLoanTypeUsedException;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.EmployeeLoanType;
import com.jchs.payrollapp.service.EmployeeLoanService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class AddEditEmployeeLoanTypeController extends AbstractController {

	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private TextField descriptionField;
	@FXML private Button deleteButton;
	
	@Parameter private EmployeeLoanType loanType;
	
	@Override
	public void updateDisplay() {
		setTitle();
		
		if (loanType != null) {
			loanType = employeeLoanService.findEmployeeLoanType(loanType.getId());
			descriptionField.setText(loanType.getDescription());
			deleteButton.setDisable(false);
		}
		
		descriptionField.requestFocus();
	}

	private void setTitle() {
		if (loanType == null) {
			stageController.setTitle("Add Employee Loan Type");
		} else {
			stageController.setTitle("Edit Employee Loan Type");
		}
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML 
	public void deleteEmployeeLoanType() {
		if (ShowDialog.confirm("Delete employee loan type?")) {
			try {
				employeeLoanService.delete(loanType);
			} catch (EmployeeLoanTypeUsedException e) {
				ShowDialog.error("Cannot delete employee loan type in use");
				return;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Employee loan type deleted");
			stageController.showEmployeeLoanTypeListScreen();
		}
	}

	@FXML
	public void saveEmployeeLoanType() {
		if (!validateFields()) {
			return;
		}
		
		if (loanType == null) {
			loanType = new EmployeeLoanType();
		}
		loanType.setDescription(descriptionField.getText());
		
		try {
			employeeLoanService.save(loanType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Employee loan type saved");
		stageController.back();
	}

	private boolean validateFields() {
		if (isDescriptionNotSpecified()) {
			ShowDialog.error("Description must be specified");
			descriptionField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isDescriptionNotSpecified() {
		return StringUtils.isEmpty(descriptionField.getText());
	}

}
