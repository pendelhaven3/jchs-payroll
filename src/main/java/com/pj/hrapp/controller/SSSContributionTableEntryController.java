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
import com.pj.hrapp.model.SSSContributionTableEntry;
import com.pj.hrapp.service.SSSService;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SSSContributionTableEntryController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(SSSContributionTableEntryController.class);
	
	@Autowired private SSSService sssService;
	
	@FXML private TextField compensationFromField;
	@FXML private TextField compensationToField;
	@FXML private TextField employeeContributionField;
	@FXML private TextField employerContributionField;
	
	@Parameter private SSSContributionTableEntry entry;
	
	@Override
	public void updateDisplay() {
		setTitle();
		
		if (entry != null) {
			entry = sssService.getSSSContributionTableEntry(entry.getId());
			
			compensationFromField.setText(FormatterUtil.formatAmount(entry.getCompensationFrom()));
			compensationToField.setText(
					entry.getCompensationTo() != null ? FormatterUtil.formatAmount(entry.getCompensationTo()) : null);
			employeeContributionField.setText(FormatterUtil.formatAmount(entry.getEmployeeContribution()));
			employerContributionField.setText(
					entry.getEmployerContribution() != null ? 
							FormatterUtil.formatAmount(entry.getEmployerContribution()) : null);
		}
		
		compensationFromField.requestFocus();
	}

	private void setTitle() {
		if (entry == null) {
			stageController.setTitle("Add New SSS Contribution Table Entry");
		} else {
			stageController.setTitle("Edit SSS Contribution Table Entry");
		}
	}

	@FXML public void saveEntry() {
		if (!validateFields()) {
			return;
		}
		
		if (entry == null) {
			entry = new SSSContributionTableEntry();
		}
		
		entry.setCompensationFrom(NumberUtil.toBigDecimal(compensationFromField.getText()));
		entry.setCompensationTo(
				isCompensationToSpecified() ? NumberUtil.toBigDecimal(compensationToField.getText()) : null);
		entry.setEmployeeContribution(NumberUtil.toBigDecimal(employeeContributionField.getText()));
		entry.setEmployerContribution(NumberUtil.toBigDecimal(employerContributionField.getText()));
		
		try {
			sssService.save(entry);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("SSS contribution table entry saved");
		stageController.showSSSContributionTableScreen();
	}

	private boolean validateFields() {
		if (isCompensationFromNotSpecified()) {
			ShowDialog.error("Compensation From must be specified");
			compensationFromField.requestFocus();
			return false;
		}
		
		if (isCompensationFromNotAValidAmount()) {
			ShowDialog.error("Compensation From must be a valid amount");
			compensationFromField.requestFocus();
			return false;
		}
		
		if (isCompensationToSpecified() && isCompensationToNotAValidAmount()) {
			ShowDialog.error("Compensation To must be a valid amount");
			compensationToField.requestFocus();
			return false;
		}
		
		if (isEmployeeContributionNotSpecified()) {
			ShowDialog.error("Employee Contribution must be specified");
			employeeContributionField.requestFocus();
			return false;
		}
		
		if (isEmployerContributionNotSpecified()) {
			ShowDialog.error("Employer Contribution must be specified");
			employerContributionField.requestFocus();
			return false;
		}
		
		if (isEmployeeContributionNotAValidAmount()) {
			ShowDialog.error("Employee Contribution must be a valid amount");
			employeeContributionField.requestFocus();
			return false;
		}
		
		if (doesCompensationRangeOverlapWithAnotherEntry()) {
			ShowDialog.error("Compensation range overlaps with another entry");
			compensationFromField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean doesCompensationRangeOverlapWithAnotherEntry() {
		SSSContributionTableEntry other = new SSSContributionTableEntry();
		other.setId(entry != null ? entry.getId() : null);
		other.setCompensationFrom(NumberUtil.toBigDecimal(compensationFromField.getText()));
		other.setCompensationTo(
				isCompensationToSpecified() ? NumberUtil.toBigDecimal(compensationToField.getText()) : null);
		
		return !sssService.getSSSContributionTable().isValidEntry(other);
	}

	private boolean isEmployeeContributionNotAValidAmount() {
		return !NumberUtil.isAmount(employeeContributionField.getText());
	}

	private boolean isEmployeeContributionNotSpecified() {
		return StringUtils.isEmpty(employeeContributionField.getText());
	}

	private boolean isEmployerContributionNotSpecified() {
		return StringUtils.isEmpty(employerContributionField.getText());
	}

	private boolean isCompensationToNotAValidAmount() {
		return !NumberUtil.isAmount(compensationToField.getText());
	}

	private boolean isCompensationFromNotAValidAmount() {
		return !NumberUtil.isAmount(compensationFromField.getText());
	}

	private boolean isCompensationToSpecified() {
		return !StringUtils.isEmpty(compensationToField.getText());
	}

	private boolean isCompensationFromNotSpecified() {
		return StringUtils.isEmpty(compensationFromField.getText());
	}

	@FXML public void cancel() {
		stageController.showSSSContributionTableScreen();
	}

	@FXML public void doOnBack() {
		stageController.showSSSContributionTableScreen();
	}

	@FXML public void deleteEntry() {
		if (!ShowDialog.confirm("Delete SSS contribution table entry?")) {
			return;
		}
		
		try {
			sssService.delete(entry);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("SSS contribution table entry deleted");
		stageController.showSSSContributionTableScreen();
	}

}
