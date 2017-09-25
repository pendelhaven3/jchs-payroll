package com.pj.hrapp.dao;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.report.LatesReportItem;
import com.pj.hrapp.model.report.SSSPhilHealthReportItem;

public interface ReportDao {

	List<SSSPhilHealthReportItem> getSSSPhilHealthReportItems(YearMonth yearMonth);

	List<LatesReportItem> getLatesReportItems(Date from, Date dateTo);

}
