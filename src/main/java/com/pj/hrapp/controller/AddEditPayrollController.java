package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.PaySchedule;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AddEditPayrollController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(AddEditPayrollController.class);
	
	@Autowired private PayrollService payrollService;
	
	@FXML private TextField batchNumberField;
	@FXML private DatePicker payDateDatePicker;
	@FXML private ComboBox<PaySchedule> payScheduleComboBox;
	@FXML private DatePicker periodCoveredFromDatePicker;
	@FXML private DatePicker periodCoveredToDatePicker;
	
	@Parameter private Payroll payroll;
	
	@Override
	public void updateDisplay() {
		setTitle();
		payScheduleComboBox.getItems().setAll(PaySchedule.values());
		
		if (payroll != null) {
			payroll = payrollService.getPayroll(payroll.getId());
			batchNumberField.setText(payroll.getBatchNumber().toString());
			payDateDatePicker.setValue(DateUtil.toLocalDate(payroll.getPayDate()));
			payScheduleComboBox.setValue(payroll.getPaySchedule());
			periodCoveredFromDatePicker.setValue(DateUtil.toLocalDate(payroll.getPeriodCoveredFrom()));
			periodCoveredToDatePicker.setValue(DateUtil.toLocalDate(payroll.getPeriodCoveredTo()));
		} else {
			batchNumberField.setText(String.valueOf(payrollService.getNextBatchNumber()));
		}
		
		batchNumberField.requestFocus();
	}

	private void setTitle() {
		if (payroll == null) {
			stageController.setTitle("Add New Payroll");
		} else {
			stageController.setTitle("Edit Payroll");
		}
	}

	@FXML public void doOnBack() {
		if (payroll == null) {
			stageController.showPayrollListScreen();
		} else {
			stageController.showPayrollScreen(payroll);
		}
	}

	@FXML public void savePayroll() {
		if (!validateFields()) {
			return;
		}
		
		if (payroll == null) {
			payroll = new Payroll();
		}
		payroll.setBatchNumber(Long.parseLong(batchNumberField.getText()));
		payroll.setPayDate(DateUtil.toDate(payDateDatePicker.getValue()));
		payroll.setPaySchedule(payScheduleComboBox.getValue());
		payroll.setPeriodCoveredFrom(DateUtil.toDate(periodCoveredFromDatePicker.getValue()));
		payroll.setPeriodCoveredTo(DateUtil.toDate(periodCoveredToDatePicker.getValue()));
		
		try {
			payrollService.save(payroll);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payroll saved");
		stageController.showPayrollScreen(payroll);
	}

	private boolean validateFields() {
		if (isBatchNumberNotSpecified()) {
			ShowDialog.error("Batch Number must be specified");
			batchNumberField.requestFocus();
			return false;
		}
		
		if (isBatchNumberAlreadyUsed()) {
			ShowDialog.error("Batch Number is already used by an existing record");
			batchNumberField.requestFocus();
			return false;
		}
		
		if (isPayDateNotSpecified()) {
			ShowDialog.error("Pay Date must be specified");
			payDateDatePicker.requestFocus();
			return false;
		}

		if (isPayScheduleNotSpecified()) {
			ShowDialog.error("Pay Schedule must be specified");
			payScheduleComboBox.requestFocus();
			return false;
		}
		
		if (isPeriodCoveredFromNotSpecified()) {
			ShowDialog.error("Period Covered From must be specified");
			periodCoveredFromDatePicker.requestFocus();
			return false;
		}

		if (isPeriodCoveredToNotSpecified()) {
			ShowDialog.error("Period Covered To must be specified");
			periodCoveredToDatePicker.requestFocus();
			return false;
		}

		return true;
	}

	private boolean isPeriodCoveredFromNotSpecified() {
		return periodCoveredFromDatePicker.getValue() == null;
	}

	private boolean isPeriodCoveredToNotSpecified() {
		return periodCoveredToDatePicker.getValue() == null;
	}

	private boolean isPayScheduleNotSpecified() {
		return payScheduleComboBox.getValue() == null;
	}

	private boolean isPayDateNotSpecified() {
		return payDateDatePicker.getValue() == null;
	}

	private boolean isBatchNumberAlreadyUsed() {
		Payroll existing = payrollService.findPayrollByBatchNumber(
				Long.parseLong(batchNumberField.getText()));
		if (existing != null) {
			if (payroll == null) {
				return true;
			} else {
				return !existing.equals(payroll);
			}
		}
		return false;
	}

	private boolean isBatchNumberNotSpecified() {
		return batchNumberField.getText().isEmpty();
	}

}
