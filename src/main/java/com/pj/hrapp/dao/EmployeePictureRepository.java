package com.pj.hrapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeePicture;

public interface EmployeePictureRepository extends JpaRepository<EmployeePicture, Long> {

	EmployeePicture findByEmployee(Employee employee);
	
}
