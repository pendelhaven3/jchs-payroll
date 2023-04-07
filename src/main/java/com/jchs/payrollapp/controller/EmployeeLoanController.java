package com.jchs.payrollapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.dialog.EmployeeLoanPaymentDialog;
import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.EmployeeLoan;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.service.EmployeeLoanService;
import com.jchs.payrollapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeLoanController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeLoanController.class);
	
	@Autowired private EmployeeLoanService employeeLoanService;
	@Autowired private EmployeeLoanPaymentDialog employeeLoanPaymentDialog;
	
	@FXML private Label employeeLabel;
	@FXML private Label loanTypeLabel;
	@FXML private Label descriptionLabel;
	@FXML private Label loanDateLabel;
	@FXML private Label amountLabel;
    @FXML private Label loanAmountLabel;
	@FXML private Label numberOfPaymentsLabel;
	@FXML private Label paymentAmountLabel;
	@FXML private Label paidLabel;
	@FXML private Label paymentStartDateLabel;
	@FXML private Text balanceText;
	@FXML private Text remarksText;
	@FXML private HBox updateLoanButtonsHBox;
	@FXML private HBox maintainPaymentsButtonsHbox;
	@FXML private Button deleteButton;
	
	@FXML private AppTableView<EmployeeLoanPayment> paymentsTable;
	
	@Parameter private EmployeeLoan loan;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Loan");
		
		loan = employeeLoanService.findEmployeeLoan(loan.getId());
		employeeLabel.setText(loan.getEmployee().toString());
		loanTypeLabel.setText(loan.getLoanType().getDescription());
		descriptionLabel.setText(loan.getDescription());
		loanDateLabel.setText(FormatterUtil.formatDate(loan.getLoanDate()));
		amountLabel.setText(FormatterUtil.formatAmount(loan.getAmount()));
        loanAmountLabel.setText(FormatterUtil.formatAmount(loan.getLoanAmount()));
		numberOfPaymentsLabel.setText(loan.getNumberOfPayments().toString());
		paymentAmountLabel.setText(FormatterUtil.formatAmount(loan.getPaymentAmount()));
		paidLabel.setText(loan.isPaid() ? "Yes" : "No");
		paymentStartDateLabel.setText(FormatterUtil.formatDate(loan.getPaymentStartDate()));
		remarksText.setText(StringUtils.defaultIfBlank(loan.getRemarks(), "(none)"));
		balanceText.setText(FormatterUtil.formatAmount(loan.getBalance()));
		
		paymentsTable.setItems(loan.getPayments());
		
		if (!loan.isPaid()) {
			paymentsTable.setDoubleClickAction(() -> updateSelectedPayment());
		}
		
		if (loan.isPaid()) {
			preventLoanFromBeingUpdated();
		}
	}

	private void preventLoanFromBeingUpdated() {
		deleteButton.setDisable(true);
		updateLoanButtonsHBox.setDisable(true);
		maintainPaymentsButtonsHbox.setDisable(true);
	}

	private void updateSelectedPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("loan", loan);
		model.put("payment", paymentsTable.getSelectedItem());
		
		employeeLoanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void doOnBack() {
		stageController.back();
	}

	@FXML 
	public void deleteLoan() {
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

	@FXML 
	public void addPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("loan", loan);
		
		employeeLoanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML 
	public void deletePayment() {
		if (!hasSelectedPayment()) {
			ShowDialog.error("No payment selected");
			return;
		}
		
		if (ShowDialog.confirm("Delete payment?")) {
			try {
				employeeLoanService.delete(paymentsTable.getSelectedItem());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Payment deleted");
			updateDisplay();
		}
	}

	private boolean hasSelectedPayment() {
		return paymentsTable.hasSelectedItem();
	}

	@FXML
	public void updateLoan() {
		stageController.showUpdateEmployeeLoanScreen(loan);
	}

	@FXML 
	public void markAsPaid() {
		if (ShowDialog.confirm("Mark employee loan as paid?")) {
			try {
				employeeLoanService.markAsPaid(loan);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Employee loan marked as paid");
			updateDisplay();
		}
	}

}
