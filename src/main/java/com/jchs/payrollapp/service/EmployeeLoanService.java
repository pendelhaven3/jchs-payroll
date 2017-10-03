package com.jchs.payrollapp.service;

import java.util.Date;
import java.util.List;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeLoan;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.search.EmployeeLoanSearchCriteria;

public interface EmployeeLoanService {

	List<EmployeeLoan> findAllUnpaidEmployeeLoans();

	EmployeeLoan findEmployeeLoan(Long id);

	void delete(EmployeeLoan loan);

	void save(EmployeeLoan loan);

	EmployeeLoanPayment findEmployeeLoanPayment(Long id);

	void save(EmployeeLoanPayment payment);

	void delete(EmployeeLoanPayment payment);

	List<EmployeeLoan> findAllUnpaidLoansByEmployee(Employee employee);

	void createLoanPayments(List<EmployeeLoan> loans, Payslip payslip);

	void markAsPaid(EmployeeLoan loan);

	List<EmployeeLoan> searchEmployeeLoans(EmployeeLoanSearchCriteria criteria);

	List<EmployeeLoan> findAllPayableLoansByEmployeeAndPaymentDate(Employee employee, Date paymentDate);

}
