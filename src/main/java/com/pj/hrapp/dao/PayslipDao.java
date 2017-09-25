package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.search.PayslipSearchCriteria;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payroll;

public interface PayslipDao {

	void deleteAllByPayroll(Payroll payroll);

	void save(Payslip payslip);

	List<Payslip> findAllByPayroll(Payroll payroll);

	Payslip get(long id);

	void delete(Payslip payslip);

	List<Payslip> search(PayslipSearchCriteria criteria);

	Payslip findAnyPayslipByEmployee(Employee employee);
	
}
