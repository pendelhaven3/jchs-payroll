package com.jchs.payrollapp.dao;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.Salary;
import com.jchs.payrollapp.model.search.SalarySearchCriteria;

public interface SalaryDao {

	List<Salary> getAllCurrent();
	
	void save(Salary salary);
	
	Salary get(long id);

	Salary findByEmployeeAndEffectiveDate(Employee employee, Date effectiveDate);

	void delete(Salary salary);

	List<Salary> search(SalarySearchCriteria criteria);

	Salary findByEmployee(Employee employee);

	BigDecimal getEmployeeCompensationForMonthYear(Employee employee, YearMonth yearMonth);
	
}
