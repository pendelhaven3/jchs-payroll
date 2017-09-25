package com.pj.hrapp.service;

import com.pj.hrapp.model.SSSContributionTable;
import com.pj.hrapp.model.SSSContributionTableEntry;

public interface SSSService {

	SSSContributionTableEntry getSSSContributionTableEntry(long id);

	void save(SSSContributionTableEntry entry);

	SSSContributionTable getSSSContributionTable();

	void delete(SSSContributionTableEntry entry);
	
}
