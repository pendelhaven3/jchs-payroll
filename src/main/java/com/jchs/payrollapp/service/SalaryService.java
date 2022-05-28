package com.jchs.payrollapp.service;

import java.util.Date;
import java.util.List;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.Salary;
import com.jchs.payrollapp.model.search.SalarySearchCriteria;

public interface SalaryService {

	List<Salary> getAllCurrentSalaries();

	void save(Salary salary);
	
	Salary getSalary(long id);

	Salary findSalaryByEmployeeAndEffectiveDate(Employee employee, Date effectiveDate);

	void delete(Salary salary);
	
	List<Salary> searchSalaries(SalarySearchCriteria criteria);
	
	Salary getCurrentSalary(Employee employee, Date currentDate);
	
}
