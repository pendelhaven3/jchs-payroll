package com.pj.hrapp.dialog;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.SelectableTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.EmployeeLoanService;

import javafx.fxml.FXML;

@Component
public class AddPayslipLoanPaymentDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(AddPayslipLoanPaymentDialog.class);
	
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private SelectableTableView<EmployeeLoan> employeeLoansTable;
	
	@Parameter private Payslip payslip;
	
	public AddPayslipLoanPaymentDialog() {
		setSceneWidth(1000d);
	}
	
	@Override
	public void updateDisplay() {
		employeeLoansTable.setItems(findAllPayableLoansByEmployeeAndNoPaymentInPayslip());
	}
	
	private List<EmployeeLoan> findAllPayableLoansByEmployeeAndNoPaymentInPayslip() {
		List<EmployeeLoan> loans = employeeLoanService.findAllPayableLoansByEmployeeAndPaymentDate(
				payslip.getEmployee(), payslip.getPayroll().getPayDate());
		
		return loans.stream().filter(loan -> {
			for (EmployeeLoanPayment loanPayment : payslip.getLoanPayments()) {
				if (loanPayment.getLoan().equals(loan)) {
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());
	}

	@Override
	protected String getDialogTitle() {
		return "Add Employee Loan Payment";
	}

	@Override
	protected String getSceneName() {
		return "addPayslipLoanPaymentDialog";
	}

	@FXML
	public void addAllLoanPayments() {
		if (employeeHasNoUnpaidLoans()) {
			ShowDialog.error("No loan payments to add");
		} else {
			addLoanPaymentsToPayslip(employeeLoansTable.getAllItems());
		}
	}

	private boolean employeeHasNoUnpaidLoans() {
		return employeeLoansTable.hasNoItems();
	}

	@FXML
	public void addSelectedLoanPayments() {
		if (hasNoLoanPaymentsSelected()) {
			ShowDialog.error("No loan payments selected");
		} else {
			addLoanPaymentsToPayslip(employeeLoansTable.getSelectedItems());
		}
	}

	private boolean hasNoLoanPaymentsSelected() {
		return !employeeLoansTable.hasSelected();
	}
	
	private void addLoanPaymentsToPayslip(List<EmployeeLoan> loans) {
		try {
			employeeLoanService.createLoanPayments(loans, payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			hide();
			return;
		}
		
		ShowDialog.info("Loan payments saved");
		hide();
	}

}
