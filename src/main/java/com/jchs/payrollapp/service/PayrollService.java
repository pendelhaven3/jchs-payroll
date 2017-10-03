package com.jchs.payrollapp.service;

import java.util.List;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.PayslipAdjustment;
import com.jchs.payrollapp.model.search.PayslipAdjustmentSearchCriteria;

public interface PayrollService {

	List<Payroll> getAllPayroll();
	
	void save(Payroll payroll);
	
	Payroll getPayroll(long id);

	Payroll findPayrollByBatchNumber(long batchNumber);

	void delete(Payroll payroll);

	Payslip getPayslip(long id);

	void save(Payslip payslip);

	void save(PayslipAdjustment payslipAdjustment);

	void delete(PayslipAdjustment payslipAdjustment);

	void delete(Payslip payslip);

	Payslip findAnyPayslipByEmployee(Employee employee);

	void postPayroll(Payroll payroll);

    void regenerateGovernmentContributions(Payslip payslip, String contributionMonth);

	void regenerateGovernmentContributions(Payroll payroll, String contributionMonth);

	long getNextBatchNumber();

    List<PayslipAdjustment> searchPayslipAdjustment(PayslipAdjustmentSearchCriteria criteria);

}
