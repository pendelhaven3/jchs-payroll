package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payroll;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	Employee findByEmployeeNumber(long employeeNumber);
	
	@Query("select e from Employee e order by e.lastName, e.firstName")
	List<Employee> findAll();

	@Query("select e from Employee e, Payroll p where e.id not in"
			+ " (select payslip.employee.id from Payroll payroll, Payslip payslip where payslip.payroll = :payroll)"
			+ " and e.paySchedule = p.paySchedule"
			+ " and p = :payroll"
			+ " and e.resigned = false"
			+ " order by e.firstName, e.lastName")
	List<Employee> findAllActiveNotInPayroll(@Param("payroll") Payroll payroll);

	@Query("select e from Employee e where e.resigned = :resigned order by e.firstName, e.lastName")
	List<Employee> findAllByResigned(@Param("resigned") boolean resigned);

	@Query("select coalesce(max(e.employeeNumber), 0) from Employee e")
	Integer findLatestEmployeeNumber();
	
}
