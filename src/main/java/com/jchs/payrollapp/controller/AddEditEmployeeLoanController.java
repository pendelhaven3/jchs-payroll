package com.jchs.payrollapp.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.exception.EmployeeAlreadyResignedException;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeLoan;
import com.jchs.payrollapp.service.EmployeeLoanService;
import com.jchs.payrollapp.service.EmployeeService;
import com.jchs.payrollapp.util.DateUtil;
import com.jchs.payrollapp.util.FormatterUtil;
import com.jchs.payrollapp.util.NumberUtil;
import com.jchs.payrollapp.model.EmployeeLoanType;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AddEditEmployeeLoanController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(AddEditEmployeeLoanController.class);
	
	@Autowired private EmployeeService employeeService;
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private ComboBox<Employee> employeeComboBox;
	@FXML private ComboBox<EmployeeLoanType> loanTypeComboBox;
	@FXML private TextField descriptionField;
	@FXML private TextField amountField;
    @FXML private TextField loanAmountField;
	@FXML private DatePicker loanDateDatePicker;
	@FXML private TextField numberOfPaymentsField;
	@FXML private TextField paymentAmountField;
	@FXML private DatePicker paymentStartDatePicker;
	@FXML private TextArea remarksTextArea;
	@FXML private Button deleteButton;
	
	@Parameter private EmployeeLoan loan;
	
	@Override
	public void updateDisplay() {
		setTitle();
		employeeComboBox.getItems().setAll(employeeService.getAllActiveEmployees());
		loanTypeComboBox.getItems().setAll(employeeLoanService.getAllEmployeeLoanTypes());
		updatePaymentAmountWhenLoanAmountChanges();
		updatePaymentAmountWhenNumberOfPaymentsChanges();
		
		if (loan != null) {
			loan = employeeLoanService.findEmployeeLoan(loan.getId());
			employeeComboBox.setValue(loan.getEmployee());
			loanTypeComboBox.setValue(loan.getLoanType());
			descriptionField.setText(loan.getDescription());
			amountField.setText(FormatterUtil.formatAmount(loan.getAmount()));
            loanAmountField.setText(FormatterUtil.formatAmount(loan.getLoanAmount()));
			loanDateDatePicker.setValue(DateUtil.toLocalDate(loan.getLoanDate()));
			numberOfPaymentsField.setText(loan.getNumberOfPayments().toString());
			paymentAmountField.setText(FormatterUtil.formatAmount(loan.getPaymentAmount()));
			paymentStartDatePicker.setValue(DateUtil.toLocalDate(loan.getPaymentStartDate()));
			remarksTextArea.setText(loan.getRemarks());
			
			deleteButton.setDisable(false);
		}
		
		employeeComboBox.requestFocus();
	}

	private void updatePaymentAmountWhenLoanAmountChanges() {
		amountField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (NumberUtil.isAmount(newValue) && NumberUtils.isDigits(numberOfPaymentsField.getText())) {
				updatePaymentAmountField(
						NumberUtil.toBigDecimal(newValue), Integer.valueOf(numberOfPaymentsField.getText()));
			} else {
				paymentAmountField.setText(null);
			}
		});
	}

	private void updatePaymentAmountField(BigDecimal loanAmount, int numberOfPayments) {
		paymentAmountField.setText(FormatterUtil.formatAmount(
				loanAmount.divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP)));
	}

	private void updatePaymentAmountWhenNumberOfPaymentsChanges() {
		numberOfPaymentsField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (NumberUtil.isAmount(amountField.getText()) && NumberUtils.isDigits(newValue)) {
				updatePaymentAmountField(
						NumberUtil.toBigDecimal(amountField.getText()), Integer.valueOf(newValue));
			} else {
				paymentAmountField.setText(null);
			}
		});
	}

	private void setTitle() {
		if (loan == null) {
			stageController.setTitle("Add Employee Loan");
		} else {
			stageController.setTitle("Edit Employee Loan");
		}
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML 
	public void deleteEmployeeLoan() {
		if (ShowDialog.confirm("Delete employee loan?")) {
			try {
				employeeLoanService.delete(loan);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Employee loan deleted");
			stageController.showEmployeeLoanListScreen();
		}
	}

	@FXML public void saveEmployeeLoan() {
		if (!validateFields()) {
			return;
		}
		
		if (loan == null) {
			loan = new EmployeeLoan();
		}
		loan.setEmployee(employeeComboBox.getValue());
		loan.setLoanType(loanTypeComboBox.getValue());
		loan.setDescription(descriptionField.getText());
		loan.setAmount(NumberUtil.toBigDecimal(amountField.getText()));
        loan.setLoanAmount(NumberUtil.toBigDecimal(loanAmountField.getText()));
		loan.setLoanDate(DateUtil.toDate(loanDateDatePicker.getValue()));
		loan.setNumberOfPayments(Integer.valueOf(numberOfPaymentsField.getText()));
		loan.setPaymentStartDate(DateUtil.toDate(paymentStartDatePicker.getValue()));
		loan.setRemarks(remarksTextArea.getText());
		
		try {
			employeeLoanService.save(loan);
		} catch (EmployeeAlreadyResignedException e) {
			ShowDialog.error("Cannot create new loan for resigned employee");
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Employee loan saved");
		stageController.back();
	}

	private boolean validateFields() {
		if (isEmployeeNotSpecified()) {
			ShowDialog.error("Employee must be specified");
			employeeComboBox.requestFocus();
			return false;
		}
		
		if (isLoanTypeNotSpecified()) {
			ShowDialog.error("Loan Type must be specified");
			loanTypeComboBox.requestFocus();
			return false;
		}
		
		if (isDescriptionNotSpecified()) {
			ShowDialog.error("Loan Description must be specified");
			descriptionField.requestFocus();
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

        if (!StringUtils.isEmpty(loanAmountField.getText()) && isLoanAmountNotValid()) {
            ShowDialog.error("Loan Amount must be a valid amount");
            loanAmountField.requestFocus();
            return false;
        }

		if (isLoanDateNotSpecified()) {
			ShowDialog.error("Loan Date must be specified");
			loanDateDatePicker.requestFocus();
			return false;
		}

		if (isNumberOfPaymentsNotSpecified()) {
			ShowDialog.error("Number of Payments must be specified");
			numberOfPaymentsField.requestFocus();
			return false;
		}
		
		if (isNumberOfPaymentsNotValid()) {
			ShowDialog.error("Number of Payments must be a valid number");
			numberOfPaymentsField.requestFocus();
			return false;
		}
		
		if (isPaymentAmountNotSpecified()) {
			ShowDialog.error("Payment Amount must be specified");
			paymentAmountField.requestFocus();
			return false;
		}
		
		if (isPaymentAmountNotValid()) {
			ShowDialog.error("Payment Amount must be a valid amount");
			paymentAmountField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isLoanTypeNotSpecified() {
		return loanTypeComboBox.getValue() == null;
	}

	private boolean isDescriptionNotSpecified() {
		return StringUtils.isEmpty(descriptionField.getText());
	}

	private boolean isPaymentAmountNotSpecified() {
		return StringUtils.isEmpty(paymentAmountField.getText());
	}

	private boolean isPaymentAmountNotValid() {
		return !NumberUtil.isAmount(paymentAmountField.getText());
	}

	private boolean isLoanDateNotSpecified() {
		return loanDateDatePicker.getValue() == null;
	}

	private boolean isNumberOfPaymentsNotValid() {
		return !NumberUtils.isDigits(numberOfPaymentsField.getText());
	}

	private boolean isNumberOfPaymentsNotSpecified() {
		return StringUtils.isEmpty(numberOfPaymentsField.getText());
	}

	private boolean isAmountNotValid() {
		return !NumberUtil.isAmount(amountField.getText());
	}

	private boolean isAmountNotSpecified() {
		return StringUtils.isEmpty(amountField.getText());
	}

    private boolean isLoanAmountNotValid() {
        return !NumberUtil.isAmount(loanAmountField.getText());
    }

	private boolean isEmployeeNotSpecified() {
		return employeeComboBox.getValue() == null;
	}

}
