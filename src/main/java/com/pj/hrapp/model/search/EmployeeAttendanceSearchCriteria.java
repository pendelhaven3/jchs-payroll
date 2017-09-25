package com.pj.hrapp.model.search;

import java.util.Date;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PaySchedule;

public class EmployeeAttendanceSearchCriteria {

	private Employee employee;
	private Date dateFrom;
	private Date dateTo;
	private PaySchedule paySchedule;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public PaySchedule getPaySchedule() {
		return paySchedule;
	}

	public void setPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
	}

}
