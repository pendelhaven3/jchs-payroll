package com.jchs.payrollapp.service;

import com.jchs.payrollapp.model.PhilHealthContributionTable;
import com.jchs.payrollapp.model.PhilHealthContributionTableEntry;

public interface PhilHealthService {

	PhilHealthContributionTableEntry getPhilHealthContributionTableEntry(long id);

	void save(PhilHealthContributionTableEntry entry);

	PhilHealthContributionTable getContributionTable();

	void delete(PhilHealthContributionTableEntry entry);
	
}
