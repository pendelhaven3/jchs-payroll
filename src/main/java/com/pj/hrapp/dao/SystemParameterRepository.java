package com.pj.hrapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pj.hrapp.model.SystemParameter;

public interface SystemParameterRepository extends JpaRepository<SystemParameter, Long> {

	SystemParameter findByName(String name);
	
}
	