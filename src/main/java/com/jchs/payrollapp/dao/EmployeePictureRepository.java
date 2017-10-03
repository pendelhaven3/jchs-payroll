package com.jchs.payrollapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeePicture;

public interface EmployeePictureRepository extends JpaRepository<EmployeePicture, Long> {

	EmployeePicture findByEmployee(Employee employee);
	
}
