package com.jchs.payrollapp.service.impl;

import static com.jchs.payrollapp.model.search.BaseSpecifications.build;
import static com.jchs.payrollapp.model.search.EmployeeSpecifications.*;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jchs.payrollapp.dao.EmployeeAttendanceDao;
import com.jchs.payrollapp.dao.EmployeePictureRepository;
import com.jchs.payrollapp.dao.EmployeeRepository;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeAttendance;
import com.jchs.payrollapp.model.EmployeePicture;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.search.EmployeeAttendanceSearchCriteria;
import com.jchs.payrollapp.model.search.EmployeeSearchCriteria;
import com.jchs.payrollapp.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired private EmployeeRepository employeeRepository;
	@Autowired private EmployeeAttendanceDao employeeAttendanceDao;
	@Autowired private EmployeePictureRepository employeePictureRepository;
	
	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Transactional
	@Override
	public void save(Employee employee) {
		employeeRepository.save(employee);
	}

	@Override
	public Employee getEmployee(long id) {
		return employeeRepository.findOne(id);
	}

	@Override
	public Employee findEmployeeByEmployeeNumber(long employeeNumber) {
		return employeeRepository.findByEmployeeNumber(employeeNumber);
	}

	@Transactional
	@Override
	public void deleteEmployee(Employee employee) {
		employeeRepository.delete(employee);
	}

	@Override
	public EmployeeAttendance getEmployeeAttendance(long id) {
		return employeeAttendanceDao.get(id);
	}

	@Transactional
	@Override
	public void save(EmployeeAttendance attendance) {
		employeeAttendanceDao.save(attendance);
	}

	@Transactional
	@Override
	public void deleteEmployeeAttendance(EmployeeAttendance employeeAttendance) {
		employeeAttendanceDao.delete(employeeAttendance);
	}

	@Override
	public List<Employee> findAllActiveEmployeesNotInPayroll(Payroll payroll) {
		return employeeRepository.findAllActiveNotInPayroll(payroll);
	}

	@Override
	public List<EmployeeAttendance> searchEmployeeAttendances(EmployeeAttendanceSearchCriteria criteria) {
		return employeeAttendanceDao.search(criteria);
	}

	@Override
	public List<Employee> getAllActiveEmployees() {
		return employeeRepository.findAllByResigned(false);
	}

	@Override
	public int getNextEmployeeNumber() {
		return employeeRepository.findLatestEmployeeNumber() + 1;
	}

	@Transactional
	@Override
	public void save(EmployeePicture employeePicture) {
		EmployeePicture fromDb = employeePictureRepository.findByEmployee(employeePicture.getEmployee());
		if (fromDb == null) {
			fromDb = employeePicture;
		} else {
			fromDb.setPicture(employeePicture.getPicture());
		}
		employeePictureRepository.save(fromDb);
	}

	@Override
	public EmployeePicture getEmployeePicture(Employee employee) {
		return employeePictureRepository.findByEmployee(employee);
	}

	@Transactional
	@Override
	public void removeEmployeePicture(Employee employee) {
		EmployeePicture employeePicture = employeePictureRepository.findByEmployee(employee);
		if (employeePicture != null) {
			employeePictureRepository.delete(employeePicture);
		}
	}

	@Override
	public EmployeeAttendance findEmployeeAttendanceByEmployeeAndDate(Employee employee, Date date) {
		return employeeAttendanceDao.findByEmployeeAndDate(employee, date);
	}

	@Override
	public List<Employee> searchEmployees(EmployeeSearchCriteria criteria) {
		Specifications<Employee> specifications = build();
		
		if (!StringUtils.isEmpty(criteria.getLastName())) {
			specifications = specifications.and(withLastName(criteria.getLastName()));
		}
		
		if (!StringUtils.isEmpty(criteria.getFirstName())) {
			specifications = specifications.and(withFirstName(criteria.getFirstName()));
		}
		
		if (criteria.getResigned() != null) {
			specifications = specifications.and(withResigned(criteria.getResigned()));
		}
		
		return employeeRepository.findAll(specifications);
	}
	
}
