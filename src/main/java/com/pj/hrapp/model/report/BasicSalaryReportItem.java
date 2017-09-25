package com.pj.hrapp.model.report;

import java.math.BigDecimal;

public class BasicSalaryReportItem {

	private String employeeName;
	private BigDecimal totalSalary;

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public BigDecimal getTotalSalary() {
		return totalSalary;
	}

	public void setTotalSalary(BigDecimal totalSalary) {
		this.totalSalary = totalSalary;
	}

}
