package com.pj.hrapp.model.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import com.pj.hrapp.util.DateUtil;

public class BasicSalaryReport {

	private YearMonth yearMonthFrom;
	private YearMonth yearMonthTo;
	private List<BasicSalaryReportItem> items = new ArrayList<>();

	public BigDecimal getTotal() {
		return items.stream()
				.map(item -> item.getTotalSalary())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}

	public String getPeriodDescription() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
		
		return new StringBuilder()
				.append(dateFormat.format(DateUtil.toDate(yearMonthFrom)))
				.append(" - ")
				.append(dateFormat.format(DateUtil.toDate(yearMonthTo)))
				.toString();
	}

	public List<BasicSalaryReportItem> getItems() {
		return items;
	}

	public void setItems(List<BasicSalaryReportItem> items) {
		this.items = items;
	}

	public YearMonth getYearMonthFrom() {
		return yearMonthFrom;
	}

	public void setYearMonthFrom(YearMonth yearMonthFrom) {
		this.yearMonthFrom = yearMonthFrom;
	}

	public YearMonth getYearMonthTo() {
		return yearMonthTo;
	}

	public void setYearMonthTo(YearMonth yearMonthTo) {
		this.yearMonthTo = yearMonthTo;
	}

}
