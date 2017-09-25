package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.PhilHealthContributionTableEntry;

public interface PhilHealthContributionTableEntryDao {

	PhilHealthContributionTableEntry get(long id);

	void save(PhilHealthContributionTableEntry entry);

	List<PhilHealthContributionTableEntry> getAll();

	void delete(PhilHealthContributionTableEntry entry);
	
}
