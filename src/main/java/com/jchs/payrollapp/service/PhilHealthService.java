package com.jchs.payrollapp.service;

import com.jchs.payrollapp.model.PhilHealthContributionTable;

public interface PhilHealthService {

	void save(PhilHealthContributionTable table);

	PhilHealthContributionTable getContributionTable();

}
