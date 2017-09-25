package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.SSSContributionTableEntry;

public interface SSSContributionTableEntryDao {

	SSSContributionTableEntry get(long id);

	void save(SSSContributionTableEntry entry);

	List<SSSContributionTableEntry> getAll();

	void delete(SSSContributionTableEntry entry);
	
}
