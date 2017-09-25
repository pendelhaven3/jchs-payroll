package com.pj.hrapp.dialog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.pj.hrapp.Constants;
import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipAdjustmentType;
import com.pj.hrapp.model.search.PayslipAdjustmentSearchCriteria;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

@Component
public class PayslipAdjustmentDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(PayslipAdjustmentDialog.class);
	
	@Autowired private PayrollService payrollService;
	
	@FXML private ComboBox<PayslipAdjustmentType> typeComboBox;
	@FXML private TextField descriptionField;
	@FXML private TextField amountField;
	@FXML private TextField contributionMonthField;
	
	@Parameter private Payslip payslip;
	@Parameter private PayslipAdjustment payslipAdjustment;
	
	@Override
	public void updateDisplay() {
		setTitle(getDialogTitle());
		typeComboBox.getItems().addAll(PayslipAdjustmentType.values());
		
		if (payslipAdjustment != null) {
			typeComboBox.setValue(payslipAdjustment.getType());
			descriptionField.setText(payslipAdjustment.getDescription());
			amountField.setText(FormatterUtil.formatAmount(payslipAdjustment.getAmount()));
			contributionMonthField.setText(payslipAdjustment.getContributionMonth());
		}

		typeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == PayslipAdjustmentType.LATES) {
				descriptionField.setText("lates/mb");
			}
		});
	}
	
	@FXML public void savePayslipAdjustment() {
		if (!validateFields()) {
			return;
		}
		
		if (payslipAdjustment == null) {
			payslipAdjustment = new PayslipAdjustment();
	        payslipAdjustment.setPayslip(payslip);
		}
		payslipAdjustment.setType(typeComboBox.getValue());
		payslipAdjustment.setDescription(descriptionField.getText());
		payslipAdjustment.setAmount(NumberUtil.toBigDecimal(amountField.getText()));
		if (payslipAdjustment.getType().isContributionType()) {
			payslipAdjustment.setContributionMonth(contributionMonthField.getText());
		}
		
		try {
			payrollService.save(payslipAdjustment);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payslip Adjustment saved");
		hide();
	}
	
	private boolean validateFields() {
		if (typeComboBox.getValue() == null) {
			ShowDialog.error("Type must be specified");
			typeComboBox.requestFocus();
			return false;
		}
		
		if (descriptionField.getText().isEmpty()) {
			ShowDialog.error("Description must be specified");
			descriptionField.requestFocus();
			return false;
		}
		
		if (amountField.getText().isEmpty()) {
			ShowDialog.error("Amount must be specified");
			amountField.requestFocus();
			return false;
		}
		
		if (!NumberUtil.isAmount(amountField.getText())) {
			ShowDialog.error("Amount must be a valid amount");
			amountField.requestFocus();
			return false;
		}
		
		if (typeComboBox.getValue().isContributionType()) {
			String contributionMonth = contributionMonthField.getText();
			if (StringUtils.isEmpty(contributionMonth)) {
				ShowDialog.error("Contribution Month must be specified (MMYYYY)");
				contributionMonthField.requestFocus();
				return false;
			}
			if (!isValidContributionMonth(contributionMonth)) {
				ShowDialog.error("Contribution Month must be a valid month (MMYYYY)");
				contributionMonthField.requestFocus();
				return false;
			}
			if (hasExistingContributionForMonth()) {
				ShowDialog.error("Employee already has contribution for the specified month");
				contributionMonthField.requestFocus();
				return false;
			}
		}
		
		return true;
	}

	private boolean isValidContributionMonth(String month) {
		return month.matches(Constants.MONTH_YEAR_REGEX);
	}
	
	private boolean hasExistingContributionForMonth() {
		PayslipAdjustmentSearchCriteria criteria = new PayslipAdjustmentSearchCriteria();
		if (payslipAdjustment == null) {
	        criteria.setEmployee(payslip.getEmployee());
		} else {
            criteria.setEmployee(payslipAdjustment.getPayslip().getEmployee());
		}
		criteria.setType(typeComboBox.getValue());
		criteria.setContributionMonth(contributionMonthField.getText());
		
		List<PayslipAdjustment> matches = payrollService.searchPayslipAdjustment(criteria);
		if (matches.isEmpty() || payslipAdjustment == null || payslipAdjustment.getId() == null) {
		    return false;
		} else {
	        return matches.stream().anyMatch(match -> !match.getId().equals(payslipAdjustment.getId()));
		}
	}

	@FXML public void cancel() {
		hide();
	}

	@Override
	protected String getDialogTitle() {
		if (payslip != null) {
			return "Add New Payslip Adjustment";
		} else {
			return "Edit Payslip Adjustment";
		}
	}

	@Override
	protected String getSceneName() {
		return "payslipAdjustment";
	}

}