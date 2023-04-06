package com.jchs.payrollapp.service;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.EmployeeLoanType;
import com.jchs.payrollapp.model.report.BasicSalaryReport;
import com.jchs.payrollapp.model.report.LatesReport;
import com.jchs.payrollapp.model.report.PagIbigReport;
import com.jchs.payrollapp.model.report.PhilHealthReport;
import com.jchs.payrollapp.model.report.SSSReport;

public interface ReportService {

	LatesReport generateLatesReport(Date from, Date to);

	BasicSalaryReport generateBasicSalaryReport(Date from, Date to);
	
	List<EmployeeLoanPayment> generateEmployeeLoanPaymentsReport(Date from, Date to, EmployeeLoanType loanType);

	PhilHealthReport generatePhilHealthReport(YearMonth yearMonth);

	SSSReport generateSSSReport(YearMonth yearMonth);

	PagIbigReport generatePagIbigReport(YearMonth yearMonth);
	
}
