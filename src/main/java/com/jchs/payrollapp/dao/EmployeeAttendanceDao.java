package com.jchs.payrollapp.dao;

import java.util.Date;
import java.util.List;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeAttendance;
import com.jchs.payrollapp.model.search.EmployeeAttendanceSearchCriteria;

public interface EmployeeAttendanceDao {

	List<EmployeeAttendance> search(EmployeeAttendanceSearchCriteria criteria);
	
	void save(EmployeeAttendance attendance);

	EmployeeAttendance get(long id);

	EmployeeAttendance findByEmployeeAndDate(Employee employee, Date date);

	void delete(EmployeeAttendance employeeAttendance);
	
}
