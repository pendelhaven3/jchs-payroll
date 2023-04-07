package com.jchs.payrollapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jchs.payrollapp.model.EmployeeLoanType;

public interface EmployeeLoanTypeRepository extends JpaRepository<EmployeeLoanType, Long> {
	
	List<EmployeeLoanType> findAllByOrderByDescriptionAsc();
	
}
