package com.pj.hrapp.service;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.EmployeeLoanPayment;
import com.pj.hrapp.model.EmployeeLoanType;
import com.pj.hrapp.model.report.BasicSalaryReport;
import com.pj.hrapp.model.report.LatesReport;
import com.pj.hrapp.model.report.SSSPhilHealthReport;

public interface ReportService {

	SSSPhilHealthReport generateSSSPhilHealthReport(YearMonth yearMonth);

	LatesReport generateLatesReport(Date from, Date to);

	BasicSalaryReport generateBasicSalaryReport(Date from, Date to);
	
	List<EmployeeLoanPayment> generateEmployeeLoanPaymentsReport(Date from, Date to, EmployeeLoanType loanType);
	
}
