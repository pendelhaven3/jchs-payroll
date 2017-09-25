package com.pj.hrapp.dao;

import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;

public interface EmployeeAttendanceDao {

	List<EmployeeAttendance> search(EmployeeAttendanceSearchCriteria criteria);
	
	void save(EmployeeAttendance attendance);

	EmployeeAttendance get(long id);

	EmployeeAttendance findByEmployeeAndDate(Employee employee, Date date);

	void delete(EmployeeAttendance employeeAttendance);
	
}
