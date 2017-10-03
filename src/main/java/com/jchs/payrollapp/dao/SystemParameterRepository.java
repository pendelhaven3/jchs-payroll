package com.jchs.payrollapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jchs.payrollapp.model.SystemParameter;

public interface SystemParameterRepository extends JpaRepository<SystemParameter, Long> {

	SystemParameter findByName(String name);
	
}
	