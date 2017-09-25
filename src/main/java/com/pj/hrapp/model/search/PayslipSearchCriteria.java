package com.pj.hrapp.model.search;

import java.util.Date;

import com.pj.hrapp.model.Employee;

public class PayslipSearchCriteria {

	private Employee employee;
	private Date payDateLessThan;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getPayDateLessThan() {
		return payDateLessThan;
	}

	public void setPayDateLessThan(Date payDateLessThan) {
		this.payDateLessThan = payDateLessThan;
	}

}
