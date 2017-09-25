package com.pj.hrapp.model.search;

import java.util.Date;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoanType;

public class EmployeeLoanSearchCriteria {

	private Employee employee;
	private Boolean paid;
	private Date paymentDate;
	private EmployeeLoanType employeeLoanType;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public EmployeeLoanType getEmployeeLoanType() {
		return employeeLoanType;
	}

	public void setEmployeeLoanType(EmployeeLoanType employeeLoanType) {
		this.employeeLoanType = employeeLoanType;
	}

}
