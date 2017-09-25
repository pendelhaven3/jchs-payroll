package com.pj.hrapp.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.Payslip;

public interface EmployeeLoanPaymentRepository extends JpaRepository<EmployeeLoanPayment, Long> {

	@Query("select p from EmployeeLoanPayment p where p.loan = :loan order by paymentNumber desc")
	List<EmployeeLoanPayment> findAllByEmployeeLoan(@Param("loan") EmployeeLoan loan);

	List<EmployeeLoanPayment> findAllByPayslip(Payslip payslip);
	
	@Query("select p from EmployeeLoanPayment p where p.paymentDate between :from and :to"
			+ " order by p.paymentDate, p.loan.employee.lastName, p.loan.employee.firstName, p.loan.description")
	List<EmployeeLoanPayment> findAllByPaymentDateBetween(@Param("from") Date from, @Param("to") Date to);
	
	@Query("select p from EmployeeLoanPayment p where p.paymentDate between :from and :to"
			+ " and p.loan.description = :loanDescription"
			+ " order by p.paymentDate, p.loan.employee.lastName, p.loan.employee.firstName, p.loan.description")
	List<EmployeeLoanPayment> findAllByPaymentDateBetweenAndLoanDescription(
			@Param("from") Date from, @Param("to") Date to, @Param("loanDescription") String loanDescription);
	

}
