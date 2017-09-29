package com.pj.hrapp.dialog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.SelectableTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.EmployeeLoanService;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.service.PayrollService;

import javafx.fxml.FXML;

@Component
public class AddPayslipDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(AddPayslipDialog.class);
	
	@Autowired private EmployeeService employeeService;
	@Autowired private PayrollService payrollService;
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@FXML private AppTableView<Employee> employeesTable;
	@FXML private SelectableTableView<EmployeeLoan> employeeLoansTable;
	
	@Parameter private Payroll payroll;
	
	private Payslip payslip;
	
	@Override
	public void updateDisplay() {
		payslip = null;
		employeesTable.getItems().setAll(employeeService.findAllActiveEmployeesNotInPayroll(payroll));
		employeesTable.setDoubleClickAction(() -> createPayslip());
	}
	
	@Override
	protected String getDialogTitle() {
		return "Add Payslip - Select Employee";
	}

	@Override
	protected String getSceneName() {
		return "addPayslipDialog";
	}

	@FXML
	public void createPayslip() {
		if (hasNoSelectedEmployee()) {
			ShowDialog.error("No employee selected");
			return;
		}
		
		if (!ShowDialog.confirm("Create payslip for employee?")) {
			hide();
			return;
		}
		
		payslip = new Payslip();
		payslip.setPayroll(payroll);
		payslip.setEmployee(employeesTable.getSelectionModel().getSelectedItem());
		payslip.setPeriodCovered(payroll.getPeriodCovered());
		
		try {
			payrollService.save(payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
//		switchToAddLoanPaymentsScreen();
		close();
	}
	
	private void switchToAddLoanPaymentsScreen() {
		changeScene("addPayslipDialog-loanPayment", 1000d, 400d);
		setTitle("Add Payslip - Add Loan Payments");
		updateEmployeeLoansTable();
	}

	private void updateEmployeeLoansTable() {
		employeeLoansTable.setItems(
				employeeLoanService.findAllPayableLoansByEmployeeAndPaymentDate(
						payslip.getEmployee(), payslip.getPayroll().getPayDate()));
	}

	private boolean hasNoSelectedEmployee() {
		return getSelectedEmployee() == null;
	}

	private Employee getSelectedEmployee() {
		return employeesTable.getSelectionModel().getSelectedItem();
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
	}

	public Payslip getPayslip() {
		return payslip;
	}
	
}