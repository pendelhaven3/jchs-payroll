package com.pj.hrapp.service;

import java.math.BigDecimal;

import com.pj.hrapp.model.CompanyProfile;

public interface SystemService {

	BigDecimal getPagibigContributionValue();
	
	void updatePagibigContributionValue(BigDecimal newValue);

	CompanyProfile getCompanyProfile();
	
	void save(CompanyProfile companyProfile);
	
}
