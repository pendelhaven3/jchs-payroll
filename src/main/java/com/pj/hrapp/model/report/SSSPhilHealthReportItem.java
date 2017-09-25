package com.pj.hrapp.model.report;

import java.math.BigDecimal;

public class SSSPhilHealthReportItem {

	private String employeeFullName;
	private String employeeNickname;
	private BigDecimal sssEmployeeContribution;
	private BigDecimal sssEmployerContribution;
	private BigDecimal monthlyPay;
	private BigDecimal pagibigContribution;
	private String sssNumber;
	private String philHealthNumber;

	public SSSPhilHealthReportItem() {
		
	}
	
	public SSSPhilHealthReportItem(
			String employeeFullName, 
			String employeeNickname, 
			BigDecimal sssEmployeeContribution,
			BigDecimal sssEmployerContribution, 
			BigDecimal monthlyPay, 
			BigDecimal pagibigContribution,
			String sssNumber,
			String philHealthNumber) {
		this.employeeFullName = employeeFullName;
		this.employeeNickname = employeeNickname;
		this.sssEmployeeContribution = sssEmployeeContribution;
		this.sssEmployerContribution = sssEmployerContribution;
		this.monthlyPay = monthlyPay;
		this.pagibigContribution = pagibigContribution;
		this.sssNumber = sssNumber;
		this.philHealthNumber = philHealthNumber;
	}

	public BigDecimal getSssTotalContribution() {
		return sssEmployeeContribution.add(sssEmployerContribution);
	}
	
	public String getEmployeeFullName() {
		return employeeFullName;
	}

	public BigDecimal getSssEmployeeContribution() {
		return sssEmployeeContribution;
	}

	public BigDecimal getSssEmployerContribution() {
		return sssEmployerContribution;
	}

	public BigDecimal getMonthlyPay() {
		return monthlyPay;
	}

	public BigDecimal getPagibigContribution() {
		return pagibigContribution;
	}

	public String getEmployeeNickname() {
		return employeeNickname;
	}
	
	public String getSssNumber() {
        return sssNumber;
    }
	
	public String getPhilHealthNumber() {
        return philHealthNumber;
    }
	
}
