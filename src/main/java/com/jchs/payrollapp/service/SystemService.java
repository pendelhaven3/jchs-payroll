package com.jchs.payrollapp.service;

import java.math.BigDecimal;

import com.jchs.payrollapp.model.CompanyProfile;

public interface SystemService {

	BigDecimal getPagibigContributionValue();
	
	void updatePagibigContributionValue(BigDecimal newValue);

	CompanyProfile getCompanyProfile();
	
	void save(CompanyProfile companyProfile);
	
}
