package com.jchs.payrollapp.dao;

import java.util.List;

import com.jchs.payrollapp.model.Payroll;

public interface PayrollDao {

	List<Payroll> getAll();
	
	void save(Payroll payroll);
	
	Payroll get(long id);

	Payroll findByBatchNumber(long batchNumber);

	void delete(Payroll payroll);

	long getLatestBatchNumber();
	
}
