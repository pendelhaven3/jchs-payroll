package com.jchs.payrollapp.dialog;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.SelectableTableView;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeLoan;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.service.EmployeeLoanService;
import com.jchs.payrollapp.service.EmployeeService;
import com.jchs.payrollapp.service.PayrollService;
import com.jchs.payrollapp.service.SalaryService;
import com.jchs.payrollapp.model.Salary;
import com.jchs.payrollapp.model.util.DateInterval;

import javafx.fxml.FXML;

@Component
public class AddPayslipDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(AddPayslipDialog.class);
	
	@Autowired private EmployeeService employeeService;
	@Autowired private PayrollService payrollService;
	@Autowired private EmployeeLoanService employeeLoanService;
	@Autowired private SalaryService salaryService;
	
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
		
		Employee employee = employeesTable.getSelectionModel().getSelectedItem();
		DateInterval periodCovered = payroll.getPeriodCovered();
		Salary salary = salaryService.getCurrentSalary(employee, periodCovered.getDateTo());
		
		payslip = new Payslip();
		payslip.setPayroll(payroll);
		payslip.setEmployee(employee);
		payslip.setPayType(salary.getPayType());
        payslip.setPaySchedule(salary.getPaySchedule());
		payslip.setPeriodCovered(payroll.getPeriodCovered());
		
		try {
			payrollService.save(payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		switchToAddLoanPaymentsScreen();
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
		close();
	}

	@FXML
	public void close() {
	    super.close();
	}
	
	public Payslip getPayslip() {
		return payslip;
	}
	
}