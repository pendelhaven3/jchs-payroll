package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pj.hrapp.model.util.DateInterval;
import com.pj.hrapp.util.DateUtil;

public class FixedRatePayslipBasicPayItem extends PayslipBasicPayItem {

	private static List<Date> nonWorkingDays = new ArrayList<>();
	
	public FixedRatePayslipBasicPayItem() {
		nonWorkingDays.add(DateUtil.toDate("12/25/2015"));
		nonWorkingDays.add(DateUtil.toDate("12/26/2015"));
	}
	
	@Override
	public BigDecimal getAmount() {
		return rate.multiply(new BigDecimal(numberOfDays))
				.divide(new BigDecimal(getTotalNumberOfWorkingDays(period)), 2, RoundingMode.HALF_UP);
	}

	private double getTotalNumberOfWorkingDays(DateInterval period) {
		return (double)period.toDateList().stream()
			.filter(date -> !DateUtil.isSunday(date) && !nonWorkingDays.contains(date))
			.count();
	}

}
