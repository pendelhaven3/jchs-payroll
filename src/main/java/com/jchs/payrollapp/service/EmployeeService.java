package com.jchs.payrollapp.service;

import java.util.Date;
import java.util.List;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeAttendance;
import com.jchs.payrollapp.model.EmployeePicture;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.search.EmployeeAttendanceSearchCriteria;
import com.jchs.payrollapp.model.search.EmployeeSearchCriteria;

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
