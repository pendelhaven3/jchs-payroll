package com.pj.hrapp.model.util;

import java.math.BigDecimal;

public class AmountInterval {

	private static final BigDecimal MAX_VALUE = BigDecimal.valueOf(9999999L);
	
	private BigDecimal amountFrom;
	private BigDecimal amountTo;

	public AmountInterval(BigDecimal amountFrom, BigDecimal amountTo) {
		this.amountFrom = amountFrom;
		this.amountTo = amountTo;
	}
	
	public boolean overlapsWith(AmountInterval other) {
		BigDecimal from1 = amountFrom != null ? amountFrom : BigDecimal.ZERO;
		BigDecimal to1 = amountTo != null ? amountTo : MAX_VALUE;
		BigDecimal from2 = other.getAmountFrom() != null ? other.getAmountFrom() : BigDecimal.ZERO;
		BigDecimal to2 = other.getAmountTo() != null ? other.getAmountTo() : MAX_VALUE;
		
		return (from1.compareTo(from2) <= 0 && from2.compareTo(to1) <= 0)
				|| (from2.compareTo(from1) <= 0 && to2.compareTo(from1) >= 0);
	}

	public BigDecimal getAmountFrom() {
		return amountFrom;
	}

	public void setAmountFrom(BigDecimal amountFrom) {
		this.amountFrom = amountFrom;
	}

	public BigDecimal getAmountTo() {
		return amountTo;
	}

	public void setAmountTo(BigDecimal amountTo) {
		this.amountTo = amountTo;
	}

}
