package com.pj.hrapp.service;

import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.EmployeePicture;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;
import com.pj.hrapp.model.search.EmployeeSearchCriteria;

public interface EmployeeService {

	List<Employee> getAllEmployees();
	
	void save(Employee employee);
	
	Employee getEmployee(long id);

	Employee findEmployeeByEmployeeNumber(long employeeNumber);

	void deleteEmployee(Employee employee);

	EmployeeAttendance getEmployeeAttendance(long id);

	void save(EmployeeAttendance attendance);

	void deleteEmployeeAttendance(EmployeeAttendance employeeAttendance);

	List<Employee> findAllActiveEmployeesNotInPayroll(Payroll payroll);
	
	List<EmployeeAttendance> searchEmployeeAttendances(EmployeeAttendanceSearchCriteria criteria);

	List<Employee> getAllActiveEmployees();

	int getNextEmployeeNumber();
	
	void save(EmployeePicture employeePicture);

	EmployeePicture getEmployeePicture(Employee employee);

	void removeEmployeePicture(Employee employee);

	EmployeeAttendance findEmployeeAttendanceByEmployeeAndDate(Employee value, Date date);

	List<Employee> searchEmployees(EmployeeSearchCriteria criteria);
	
}
