package com.jchs.payrollapp.model.report;

import java.time.YearMonth;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayrollReport {

	private YearMonth yearMonth;
	private List<PayrollReportItem> items;
	
}
