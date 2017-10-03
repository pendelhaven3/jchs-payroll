package com.jchs.payrollapp.dao;

import java.util.List;

import com.jchs.payrollapp.model.PhilHealthContributionTableEntry;

public interface PhilHealthContributionTableEntryDao {

	PhilHealthContributionTableEntry get(long id);

	void save(PhilHealthContributionTableEntry entry);

	List<PhilHealthContributionTableEntry> getAll();

	void delete(PhilHealthContributionTableEntry entry);
	
}
