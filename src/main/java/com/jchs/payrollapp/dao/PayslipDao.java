package com.jchs.payrollapp.dao;

import java.util.List;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.search.PayslipSearchCriteria;

public interface PayslipDao {

	void deleteAllByPayroll(Payroll payroll);

	void save(Payslip payslip);

	List<Payslip> findAllByPayroll(Payroll payroll);

	Payslip get(long id);

	void delete(Payslip payslip);

	List<Payslip> search(PayslipSearchCriteria criteria);

	Payslip findAnyPayslipByEmployee(Employee employee);
	
}
