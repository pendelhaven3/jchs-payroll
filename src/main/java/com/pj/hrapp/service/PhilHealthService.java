package com.pj.hrapp.service;

import com.pj.hrapp.model.PhilHealthContributionTable;
import com.pj.hrapp.model.PhilHealthContributionTableEntry;

public interface PhilHealthService {

	PhilHealthContributionTableEntry getPhilHealthContributionTableEntry(long id);

	void save(PhilHealthContributionTableEntry entry);

	PhilHealthContributionTable getContributionTable();

	void delete(PhilHealthContributionTableEntry entry);
	
}
