package com.jchs.payrollapp.model.report;

import com.jchs.payrollapp.model.Employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayrollReportItem {

	private Employee employee;
	private SSSReportItem sssReportItem;
	private PhilHealthReportItem philHealthReportItem;
	private PagIbigReportItem pagIbigReportItem;
	
}
