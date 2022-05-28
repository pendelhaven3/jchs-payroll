package com.jchs.payrollapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixedRatePayslipBasicPayItem extends PayslipBasicPayItem {

	@Override
	public BigDecimal getAmount() {
		return rate.multiply(new BigDecimal(numberOfDays))
				.divide(new BigDecimal(period.getNumberOfDays()), 2, RoundingMode.HALF_UP);
	}

}
