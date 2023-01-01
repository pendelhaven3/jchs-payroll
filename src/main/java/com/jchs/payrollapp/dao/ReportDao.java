package com.jchs.payrollapp.dao;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import com.jchs.payrollapp.model.report.LatesReportItem;
import com.jchs.payrollapp.model.report.PagIbigReportItem;
import com.jchs.payrollapp.model.report.PhilHealthReportItem;
import com.jchs.payrollapp.model.report.SSSPhilHealthReportItem;
import com.jchs.payrollapp.model.report.SSSReportItem;

public interface ReportDao {

	List<SSSPhilHealthReportItem> getSSSPhilHealthReportItems(YearMonth yearMonth);

	List<LatesReportItem> getLatesReportItems(Date from, Date dateTo);

	List<PhilHealthReportItem> getPhilHealthReportItems(YearMonth yearMonth);

	List<SSSReportItem> getSSSReportItems(YearMonth yearMonth);

	List<PagIbigReportItem> getPagIbigReportItems(YearMonth yearMonth);

}
