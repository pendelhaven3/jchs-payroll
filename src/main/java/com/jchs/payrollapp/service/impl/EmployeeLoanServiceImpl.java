package com.jchs.payrollapp.service.impl;

import static com.jchs.payrollapp.model.search.BaseSpecifications.build;
import static com.jchs.payrollapp.model.search.EmployeeLoanSpecifications.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jchs.payrollapp.dao.EmployeeLoanPaymentRepository;
import com.jchs.payrollapp.dao.EmployeeLoanRepository;
import com.jchs.payrollapp.exception.EmployeeAlreadyResignedException;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeLoan;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.EmployeeLoanType;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.search.EmployeeLoanSearchCriteria;
import com.jchs.payrollapp.service.EmployeeLoanService;
import com.jchs.payrollapp.exception.EmployeeLoanTypeUsedException;
import com.jchs.payrollapp.dao.EmployeeLoanTypeRepository;

@Service
public class EmployeeLoanServiceImpl implements EmployeeLoanService {

	@Autowired private EmployeeLoanRepository employeeLoanRepository;
	@Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;
	@Autowired private EmployeeLoanTypeRepository employeeLoanTypeRepository;
	
	@Override
	public List<EmployeeLoan> findAllUnpaidEmployeeLoans() {
		List<EmployeeLoan> loans = employeeLoanRepository.findAllByPaid(false);
		for (EmployeeLoan loan : loans) {
			loan.setPayments(employeeLoanPaymentRepository.findAllByEmployeeLoan(loan));
		}
		return loans;
	}

	@Override
	public EmployeeLoan findEmployeeLoan(Long id) {
		EmployeeLoan loan = employeeLoanRepository.findOne(id);
		loan.setPayments(employeeLoanPaymentRepository.findAllByEmployeeLoan(loan));
		return loan;
	}

	@Transactional
	@Override
	public void delete(EmployeeLoan loan) {
		employeeLoanRepository.delete(loan);
	}

	@Transactional
	@Override
	public void save(EmployeeLoan loan) {
		if (loan.isNew() && loan.getEmployee().isResigned()) {
			throw new EmployeeAlreadyResignedException();
		}
		employeeLoanRepository.save(loan);
	}

	@Override
	public EmployeeLoanPayment findEmployeeLoanPayment(Long id) {
		return employeeLoanPaymentRepository.findOne(id);
	}

	@Transactional
	@Override
	public void save(EmployeeLoanPayment payment) {
		employeeLoanPaymentRepository.save(payment);
	}

	@Transactional
	@Override
	public void delete(EmployeeLoanPayment payment) {
		employeeLoanPaymentRepository.delete(payment);
	}

	@Override
	public List<EmployeeLoan> findAllUnpaidLoansByEmployee(Employee employee) {
		List<EmployeeLoan> loans = employeeLoanRepository.findAllByEmployeeAndPaid(employee, false);
		for (EmployeeLoan loan : loans) {
			loan.setPayments(employeeLoanPaymentRepository.findAllByEmployeeLoan(loan));
		}
		return loans;
	}

	@Transactional
	@Override
	public void createLoanPayments(List<EmployeeLoan> loans, Payslip payslip) {
		List<EmployeeLoanPayment> payments = new ArrayList<>();
		for (EmployeeLoan loan : loans) {
			loan = findEmployeeLoan(loan.getId());
			
			EmployeeLoanPayment payment = new EmployeeLoanPayment();
			payment.setPayslip(payslip);
			payment.setLoan(loan);
			payment.setPaymentDate(payslip.getPayroll().getPayDate());
			payment.setAmount(loan.getPaymentAmount());
			payment.setPaymentNumber(loan.getNextPaymentNumber());
			payments.add(payment);
		}
		employeeLoanPaymentRepository.save(payments);
	}

	@Transactional
	@Override
	public void markAsPaid(EmployeeLoan loan) {
		loan.setPaid(true);
		employeeLoanRepository.save(loan);
	}

	@Override
	public List<EmployeeLoan> searchEmployeeLoans(EmployeeLoanSearchCriteria criteria) {
		Specifications<EmployeeLoan> specifications = build();
		
		if (criteria.getEmployee() != null) {
			specifications = specifications.and(withEmployee(criteria.getEmployee()));
		}
		
		if (criteria.getEmployeeLoanType() != null) {
			specifications = specifications.and(withLoanType(criteria.getEmployeeLoanType()));
		}
		
		if (criteria.getPaid() != null) {
			specifications = specifications.and(withPaid(criteria.getPaid()));
		}
		
		if (criteria.getPaymentDate() != null) {
			specifications = specifications.and(withPaymentDate(criteria.getPaymentDate()));
		}
		
		List<EmployeeLoan> loans = employeeLoanRepository.findAll(specifications);
		for (EmployeeLoan loan : loans) {
			loan.setPayments(employeeLoanPaymentRepository.findAllByEmployeeLoan(loan));
		}
		return loans;
	}

	@Override
	public List<EmployeeLoan> findAllPayableLoansByEmployeeAndPaymentDate(Employee employee, Date paymentDate) {
		EmployeeLoanSearchCriteria criteria = new EmployeeLoanSearchCriteria();
		criteria.setEmployee(employee);
		criteria.setPaid(false);
		criteria.setPaymentDate(paymentDate);
		
		return searchEmployeeLoans(criteria);
	}

	@Override
	public List<EmployeeLoanType> getAllEmployeeLoanTypes() {
		return employeeLoanTypeRepository.findAllByOrderByDescriptionAsc();
	}

	@Override
	public EmployeeLoanType findEmployeeLoanType(Long id) {
		return employeeLoanTypeRepository.findOne(id);
	}

	@Override
	public void save(EmployeeLoanType loanType) {
		employeeLoanTypeRepository.save(loanType);
	}

	@Override
	public void delete(EmployeeLoanType loanType) {
		if (employeeLoanRepository.findFirstByLoanType(loanType) != null) {
			throw new EmployeeLoanTypeUsedException();
		}
		
		employeeLoanTypeRepository.delete(loanType);
	}

}
