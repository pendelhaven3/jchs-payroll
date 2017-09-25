package com.pj.hrapp.model.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LatesReport {

	private Date dateFrom;
	private Date dateTo;
	private List<LatesReportItem> items;

	public BigDecimal getTotalLates() {
		return new BigDecimal(items.stream().map(item -> item.getLates())
				.reduce(0, (x,y) -> x + y));
	}
	
	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public List<LatesReportItem> getItems() {
		return items;
	}

	public void setItems(List<LatesReportItem> items) {
		this.items = items;
	}

}
