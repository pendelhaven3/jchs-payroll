package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.model.search.PayslipAdjustmentSearchCriteria;

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

	void delete(ValeProduct valeProduct);

	Payslip findAnyPayslipByEmployee(Employee employee);

	void postPayroll(Payroll payroll);

    void regenerateGovernmentContributions(Payslip payslip, String contributionMonth);

	void regenerateGovernmentContributions(Payroll payroll, String contributionMonth);

	long getNextBatchNumber();

    List<PayslipAdjustment> searchPayslipAdjustment(PayslipAdjustmentSearchCriteria criteria);

}
