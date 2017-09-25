package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.AppDatePicker;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.service.SalaryService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SalaryController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);
	
	@Autowired private EmployeeService employeeService;
	@Autowired private SalaryService salaryService;
	
	@FXML ComboBox<Employee> employeeComboBox;
	@FXML TextField rateField;
	@FXML AppDatePicker effectiveDateFromDatePicker;
	@FXML AppDatePicker effectiveDateToDatePicker;
	@FXML private Button deleteButton;
	
	@Parameter private Salary salary;
	
	@Override
	public void updateDisplay() {
		if (salary == null) {
			stageController.setTitle("Add Salary");
		} else {
			stageController.setTitle("Edit Salary");
		}
		
		employeeComboBox.getItems().setAll(employeeService.getAllEmployees());
		
		if (salary != null) {
			salary = salaryService.getSalary(salary.getId());
			employeeComboBox.setValue(salary.getEmployee());
			rateField.setText(FormatterUtil.formatAmount(salary.getRate()));
			effectiveDateFromDatePicker.setValue(DateUtil.toLocalDate(salary.getEffectiveDateFrom()));
			if (salary.getEffectiveDateTo() != null) {
				effectiveDateToDatePicker.setValue(DateUtil.toLocalDate(salary.getEffectiveDateTo()));
			}
			
			deleteButton.setDisable(false);
		}
		
		// TODO: Handle resigned employee
		
		employeeComboBox.requestFocus();
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML public void deleteSalary() {
		if (ShowDialog.confirm("Delete salary?")) {
			try {
				salaryService.delete(salary);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
			}
			
			ShowDialog.info("Salary deleted");
			stageController.showSalaryListScreen();
		}
	}

	@FXML public void saveSalary() {
		if (!validateFields()) {
			return;
		}
		
		if (salary == null) {
			salary = new Salary();
		}
		salary.setEmployee(employeeComboBox.getValue());
		salary.setRate(NumberUtil.toBigDecimal(rateField.getText()));
		salary.setEffectiveDateFrom(DateUtil.toDate(effectiveDateFromDatePicker.getValue()));
		if (effectiveDateToDatePicker.getValue() != null) {
			salary.setEffectiveDateTo(DateUtil.toDate(effectiveDateToDatePicker.getValue()));
		} else {
			salary.setEffectiveDateTo(null);
		}
		
		try {
			salaryService.save(salary);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
		}
		
		ShowDialog.info("Salary saved");
		stageController.back();
	}

	private boolean validateFields() {
		if (isEmployeeNotSpecified()) {
			ShowDialog.error("Employee must be specified");
			employeeComboBox.requestFocus();
			return false;
		}
		
		if (isRateNotSpecified()) {
			ShowDialog.error("Rate must be specified");
			rateField.requestFocus();
			return false;
		}
		
		if (isRateNotValid()) {
			ShowDialog.error("Rate must be a valid amount");
			rateField.requestFocus();
			return false;
		}

		if (isEffectiveDateFromNotSpecified()) {
			ShowDialog.error("Effective Date From must be specified");
			effectiveDateFromDatePicker.requestFocus();
			return false;
		}
		
		if (isSalaryAlreadyDefinedForEffectiveDatePeriod()) {
			ShowDialog.error("Salary is already defined for effective date period");
			effectiveDateFromDatePicker.requestFocus();
			return false;
		}
		
		if (isEffectiveDateToSpecified() && !isEffectiveDateToGreaterThanEffectiveDateFrom()) {
			ShowDialog.error("Effective Date To must be greater than Effective Date From");
			effectiveDateToDatePicker.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isSalaryAlreadyDefinedForEffectiveDatePeriod() {
		Salary existing = salaryService.findSalaryByEmployeeAndEffectiveDate(
				employeeComboBox.getValue(),
				DateUtil.toDate(effectiveDateFromDatePicker.getValue()));
		if (existing != null) {
			if (salary == null) {
				return true;
			} else {
				return !existing.equals(salary);
			}
		}
		return false;
	}

	private boolean isEffectiveDateToGreaterThanEffectiveDateFrom() {
		return effectiveDateToDatePicker.getValue().compareTo(
				effectiveDateFromDatePicker.getValue()) > 0;
	}

	private boolean isEffectiveDateToSpecified() {
		return effectiveDateToDatePicker.getValue() != null;
	}

	private boolean isEffectiveDateFromNotSpecified() {
		return effectiveDateFromDatePicker.getValue() == null;
	}

	private boolean isRateNotValid() {
		return !NumberUtil.isAmount(rateField.getText());
	}

	private boolean isRateNotSpecified() {
		return rateField.getText().isEmpty();
	}

	private boolean isEmployeeNotSpecified() {
		return employeeComboBox.getValue() == null;
	}

}
