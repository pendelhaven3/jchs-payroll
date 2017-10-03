package com.jchs.payrollapp.dialog;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeLoan;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.service.EmployeeLoanService;
import com.jchs.payrollapp.util.DateUtil;
import com.jchs.payrollapp.util.FormatterUtil;
import com.jchs.payrollapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@Component
public class EmployeeLoanPaymentDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeLoanPaymentDialog.class);
	
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private ComboBox<EmployeeLoan> employeeLoanComboBox;
	@FXML private TextField paymentNumberField;
	@FXML private DatePicker paymentDateDatePicker;
	@FXML private TextField amountField;
	
	@Parameter private Payslip payslip;
	@Parameter private EmployeeLoan loan;
	@Parameter private EmployeeLoanPayment payment;
	
	@Override
	protected String getDialogTitle() {
		if (payment == null) {
			return "Add Employee Loan Payment";
		} else {
			return "Edit Employee Loan Payment";
		}
	}

	@Override
	protected void updateDisplay() {
		employeeLoanComboBox.getItems().setAll(findAllUnpaidLoansByEmployee());
		
		if (payment != null) {
			payment = employeeLoanService.findEmployeeLoanPayment(payment.getId());
			employeeLoanComboBox.setValue(payment.getLoan());
			paymentNumberField.setText(payment.getPaymentNumber().toString());
			paymentDateDatePicker.setValue(DateUtil.toLocalDate(payment.getPaymentDate()));
			amountField.setText(FormatterUtil.formatAmount(payment.getAmount()));
		} else if (loan != null) {
			employeeLoanComboBox.setValue(loan);
			paymentNumberField.setText(loan.getNextPaymentNumber().toString());
			amountField.setText(FormatterUtil.formatAmount(loan.getPaymentAmount()));
		} else if (payslip != null) {
			employeeLoanComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
				EmployeeLoan loan = employeeLoanService.findEmployeeLoan(newValue.getId());
				paymentNumberField.setText(loan.getNextPaymentNumber().toString());
				paymentDateDatePicker.setValue(DateUtil.toLocalDate(payslip.getPayroll().getPayDate()));
				amountField.setText(FormatterUtil.formatAmount(loan.getPaymentAmount()));
			});
		}
		
		employeeLoanComboBox.setDisable(loan != null);
		paymentNumberField.setDisable(payslip != null);
		paymentDateDatePicker.setDisable(payslip != null);
		amountField.setDisable(payslip != null);
	}

	private List<EmployeeLoan> findAllUnpaidLoansByEmployee() {
		Employee employee = null;
		if (loan != null) {
			employee = loan.getEmployee();
		} else if (payslip != null) {
			employee = payslip.getEmployee();
		} else {
			return Collections.emptyList();
		}
		
		return employeeLoanService.findAllUnpaidLoansByEmployee(employee);
	}

	@Override
	protected String getSceneName() {
		return "employeeLoanPaymentDialog";
	}

	@FXML 
	public void saveEmployeeLoanPayment() {
		if (!validateFields()) {
			return;
		}
		
		if (payment == null) {
			payment = new EmployeeLoanPayment();
		}
		payment.setLoan(employeeLoanComboBox.getValue());
		payment.setPaymentNumber(Integer.valueOf(paymentNumberField.getText()));
		payment.setPaymentDate(DateUtil.toDate(paymentDateDatePicker.getValue()));
		payment.setAmount(NumberUtil.toBigDecimal(amountField.getText()));
		payment.setPayslip(payslip);;
		
		try {
			employeeLoanService.save(payment);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Employee loan payment saved");
		hide();
	}

	private boolean validateFields() {
		if (isEmployeeLoanNotSpecified()) {
			ShowDialog.error("Employee Loan must be specified");
			employeeLoanComboBox.requestFocus();
			return false;
		}
		
		if (isPaymentNumberNotSpecified()) {
			ShowDialog.error("Payment Number must be specified");
			paymentNumberField.requestFocus();
			return false;
		}
		
		if (isPaymentNumberNotValid()) {
			ShowDialog.error("Payment Number must be a valid number");
			paymentNumberField.requestFocus();
			return false;
		}
		
		if (isAmountNotSpecified()) {
			ShowDialog.error("Amount must be specified");
			amountField.requestFocus();
			return false;
		}

		if (isAmountNotValid()) {
			ShowDialog.error("Amount must be a valid amount");
			amountField.requestFocus();
			return false;
		}
		
		if (isPaymentDateNotSpecified()) {
			ShowDialog.error("Payment Date must be specified");
			paymentDateDatePicker.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isEmployeeLoanNotSpecified() {
		return employeeLoanComboBox.getValue() == null;
	}

	private boolean isAmountNotSpecified() {
		return StringUtils.isEmpty(amountField.getText());
	}

	private boolean isAmountNotValid() {
		return !NumberUtil.isAmount(amountField.getText());
	}

	private boolean isPaymentDateNotSpecified() {
		return paymentDateDatePicker.getValue() == null;
	}

	private boolean isPaymentNumberNotValid() {
		return !NumberUtils.isDigits(paymentNumberField.getText());
	}

	private boolean isPaymentNumberNotSpecified() {
		return StringUtils.isEmpty(paymentNumberField.getText());
	}

}
