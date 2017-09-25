package com.pj.hrapp.model;

import java.util.Arrays;
import java.util.List;

public enum PayslipAdjustmentType {

	BONUS,
	COMPANY_LOAN,
	INCENTIVE,
	LATES,
	OTHERS,
	OVERTIME,
	PAGIBIG,
	PAGIBIG_LOAN,
	PHILHEALTH,
	SSS,
	SSS_LOAN,
	VALE_CASH,
	VALE_PRODUCT;
	
	private static List<PayslipAdjustmentType> CONTRIBUTION_TYPES = Arrays.asList(SSS, PHILHEALTH, PAGIBIG);
	
	public boolean isContributionType() {
		return CONTRIBUTION_TYPES.contains(this);
	}
	
}
