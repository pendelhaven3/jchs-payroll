package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;

public interface EmployeeLoanRepository extends JpaRepository<EmployeeLoan, Long>, 
		JpaSpecificationExecutor<EmployeeLoan> {

	@Query("select l from EmployeeLoan l where l.paid = :paid order by l.employee.lastName, l.employee.firstName")
	List<EmployeeLoan> findAllByPaid(@Param("paid") Boolean paid);

	List<EmployeeLoan> findAllByEmployeeAndPaid(Employee employee, boolean paid);
	
}
