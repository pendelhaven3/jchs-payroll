package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Payroll;

public interface PayrollDao {

	List<Payroll> getAll();
	
	void save(Payroll payroll);
	
	Payroll get(long id);

	Payroll findByBatchNumber(long batchNumber);

	void delete(Payroll payroll);

	long getLatestBatchNumber();
	
}
