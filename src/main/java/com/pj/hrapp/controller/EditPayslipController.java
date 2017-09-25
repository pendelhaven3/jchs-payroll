package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EditPayslipController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(EditPayslipController.class);
	
	@Autowired private PayrollService payrollService;
	
	@FXML private Label payrollBatchNumberLabel;
	@FXML private Label employeeLabel;
	@FXML private DatePicker periodCoveredFromDatePicker;
	@FXML private DatePicker periodCoveredToDatePicker;
	
	@Parameter private Payslip payslip;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Edit Payslip");
		
		if (payslip != null) {
			payslip = payrollService.getPayslip(payslip.getId());
			payrollBatchNumberLabel.setText(payslip.getPayroll().getBatchNumber().toString());
			employeeLabel.setText(payslip.getEmployee().getNickname());
			periodCoveredFromDatePicker.setValue(DateUtil.toLocalDate(payslip.getPeriodCoveredFrom()));
			periodCoveredToDatePicker.setValue(DateUtil.toLocalDate(payslip.getPeriodCoveredTo()));
		}
		
		periodCoveredFromDatePicker.requestFocus();
	}

	@FXML public void doOnBack() {
		stageController.showPayslipScreen(payslip);
	}

	@FXML public void savePayslip() {
		if (!validateFields()) {
			return;
		}
		
		if (payslip == null) {
			payslip = new Payslip();
		}
		payslip.setPeriodCoveredFrom(DateUtil.toDate(periodCoveredFromDatePicker.getValue()));
		payslip.setPeriodCoveredTo(DateUtil.toDate(periodCoveredToDatePicker.getValue()));
		
		try {
			payrollService.save(payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payslip saved");
		stageController.showPayslipScreen(payslip);
	}

	private boolean validateFields() {
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
		
		if (isPeriodCoveredToGreaterThanFrom()) {
			ShowDialog.error("Period Covered To must be greater than or equal to From");
			periodCoveredToDatePicker.requestFocus();
			return false;
		}

		return true;
	}

	private boolean isPeriodCoveredToGreaterThanFrom() {
		return periodCoveredToDatePicker.getValue().isBefore(periodCoveredFromDatePicker.getValue());
	}

	private boolean isPeriodCoveredFromNotSpecified() {
		return periodCoveredFromDatePicker.getValue() == null;
	}

	private boolean isPeriodCoveredToNotSpecified() {
		return periodCoveredToDatePicker.getValue() == null;
	}

}
