package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PerDayPayslipBasicPayItem extends PayslipBasicPayItem {

	@Override
	public BigDecimal getAmount() {
		return rate.multiply(new BigDecimal(numberOfDays)).setScale(2, RoundingMode.HALF_UP);
	}

}
