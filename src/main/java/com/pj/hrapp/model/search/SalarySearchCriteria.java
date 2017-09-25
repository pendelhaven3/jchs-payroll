package com.pj.hrapp.model.search;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.PaySchedule;

public class SalarySearchCriteria {

	private Employee employee;
	private Date effectiveDateFrom;
	private Date effectiveDateTo;
	private PaySchedule paySchedule;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getEffectiveDateFrom() {
		return effectiveDateFrom;
	}

	public void setEffectiveDateFrom(Date effectiveDateFrom) {
		this.effectiveDateFrom = effectiveDateFrom;
	}

	public Date getEffectiveDateTo() {
		return effectiveDateTo;
	}

	public void setEffectiveDateTo(Date effectiveDateTo) {
		this.effectiveDateTo = effectiveDateTo;
	}

	public PaySchedule getPaySchedule() {
		return paySchedule;
	}

	public void setPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("employee", employee);
		map.put("paySchedule", paySchedule);
		return map;
	}

}
