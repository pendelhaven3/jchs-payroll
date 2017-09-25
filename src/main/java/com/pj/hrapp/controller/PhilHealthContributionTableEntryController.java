package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.PhilHealthContributionTableEntry;
import com.pj.hrapp.service.PhilHealthService;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhilHealthContributionTableEntryController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(PhilHealthContributionTableEntryController.class);
	
	@Autowired private PhilHealthService philHealthService;
	
	@FXML private TextField salaryFromField;
	@FXML private TextField salaryToField;
	@FXML private TextField employeeShareField;
	
	@Parameter private PhilHealthContributionTableEntry entry;
	
	@Override
	public void updateDisplay() {
		setTitle();
		
		if (entry != null) {
			salaryFromField.setText(
					entry.getSalaryFrom() != null ? FormatterUtil.formatAmount(entry.getSalaryFrom()) : null);
			salaryToField.setText(
					entry.getSalaryTo() != null ? FormatterUtil.formatAmount(entry.getSalaryTo()) : null);
			employeeShareField.setText(FormatterUtil.formatAmount(entry.getEmployeeShare()));
		}
		
		salaryFromField.requestFocus();
	}

	private void setTitle() {
		if (entry == null) {
			stageController.setTitle("Add New PhilHealth Contribution Table Entry");
		} else {
			stageController.setTitle("Edit PhilHealth Contribution Table Entry");
		}
	}

	@FXML public void saveEntry() {
		if (!validateFields()) {
			return;
		}
		
		if (entry == null) {
			entry = new PhilHealthContributionTableEntry();
		}
		
		entry.setSalaryFrom(
				isSalaryFromSpecified() ? NumberUtil.toBigDecimal(salaryFromField.getText()) : null);
		entry.setSalaryTo(
				isSalaryToSpecified() ? NumberUtil.toBigDecimal(salaryToField.getText()) : null);
		entry.setEmployeeShare(NumberUtil.toBigDecimal(employeeShareField.getText()));
		
		try {
			philHealthService.save(entry);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("PhilHealth contribution table entry saved");
		stageController.showPhilHealthContributionTableScreen();
	}

	private boolean validateFields() {
		if (isSalaryFromAndToNotSpecified()) {
			ShowDialog.error("Salary From and/or Salary To must be specified");
			salaryFromField.requestFocus();
			return false;
		}
		
		if (isSalaryFromSpecified() && isCompensationFromNotAValidAmount()) {
			ShowDialog.error("Salary From must be a valid amount");
			salaryFromField.requestFocus();
			return false;
		}
		
		if (isSalaryToSpecified() && isCompensationToNotAValidAmount()) {
			ShowDialog.error("Salary To must be a valid amount");
			salaryToField.requestFocus();
			return false;
		}
		
		if (isEmployeeShareNotSpecified()) {
			ShowDialog.error("Employee Share must be specified");
			employeeShareField.requestFocus();
			return false;
		}
		
		if (isEmployeeShareNotAValidAmount()) {
			ShowDialog.error("Employee Share must be a valid amount");
			employeeShareField.requestFocus();
			return false;
		}
		
		if (doesSalaryRangeOverlapWithAnotherEntry()) {
			ShowDialog.error("Salary range overlaps with another entry");
			salaryFromField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isSalaryFromAndToNotSpecified() {
		return !isSalaryFromSpecified() && !isSalaryToSpecified();
	}

	private boolean doesSalaryRangeOverlapWithAnotherEntry() {
		PhilHealthContributionTableEntry other = new PhilHealthContributionTableEntry();
		other.setId(entry != null ? entry.getId() : null);
		other.setSalaryFrom(
				isSalaryFromSpecified() ? NumberUtil.toBigDecimal(salaryFromField.getText()) : null);
		other.setSalaryTo(
				isSalaryToSpecified() ? NumberUtil.toBigDecimal(salaryToField.getText()) : null);
		
		return !philHealthService.getContributionTable().isValidEntry(other);
	}

	private boolean isEmployeeShareNotAValidAmount() {
		return !NumberUtil.isAmount(employeeShareField.getText());
	}

	private boolean isEmployeeShareNotSpecified() {
		return StringUtils.isEmpty(employeeShareField.getText());
	}

	private boolean isCompensationToNotAValidAmount() {
		return !NumberUtil.isAmount(salaryToField.getText());
	}

	private boolean isCompensationFromNotAValidAmount() {
		return !NumberUtil.isAmount(salaryFromField.getText());
	}

	private boolean isSalaryToSpecified() {
		return !StringUtils.isEmpty(salaryToField.getText());
	}

	private boolean isSalaryFromSpecified() {
		return !StringUtils.isEmpty(salaryFromField.getText());
	}

	@FXML public void cancel() {
		stageController.showPhilHealthContributionTableScreen();
	}

	@FXML public void doOnBack() {
		stageController.showPhilHealthContributionTableScreen();
	}

	@FXML public void deleteEntry() {
		if (!ShowDialog.confirm("Delete PhilHealth contribution table entry?")) {
			return;
		}
		
		try {
			philHealthService.delete(entry);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("PhilHealth contribution table entry deleted");
		stageController.showPhilHealthContributionTableScreen();
	}

}
