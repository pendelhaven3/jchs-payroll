package com.jchs.payrollapp.dao;

import java.util.List;

import com.jchs.payrollapp.model.SSSContributionTableEntry;

public interface SSSContributionTableEntryDao {

	SSSContributionTableEntry get(long id);

	void save(SSSContributionTableEntry entry);

	List<SSSContributionTableEntry> getAll();

	void delete(SSSContributionTableEntry entry);
	
}
