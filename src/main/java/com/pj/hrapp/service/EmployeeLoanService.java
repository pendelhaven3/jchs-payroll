package com.pj.hrapp.service;

import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.search.EmployeeLoanSearchCriteria;

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
