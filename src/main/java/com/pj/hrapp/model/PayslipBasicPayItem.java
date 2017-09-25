package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.pj.hrapp.model.util.DateInterval;

public abstract class PayslipBasicPayItem {

	protected BigDecimal rate;
	protected DateInterval period;
	protected double numberOfDays;

	public abstract BigDecimal getAmount();
	
	public String getPeriodAsString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");
		
		return new StringBuilder()
			.append(dateFormat.format(period.getDateFrom()))
			.append(" - ")
			.append(dateFormat.format(period.getDateTo()))
			.toString();
	}
	
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public DateInterval getPeriod() {
		return period;
	}

	public void setPeriod(DateInterval period) {
		this.period = period;
	}

	public double getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

}