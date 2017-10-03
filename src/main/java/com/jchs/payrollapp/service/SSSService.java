package com.jchs.payrollapp.service;

import com.jchs.payrollapp.model.SSSContributionTable;
import com.jchs.payrollapp.model.SSSContributionTableEntry;

public interface SSSService {

	SSSContributionTableEntry getSSSContributionTableEntry(long id);

	void save(SSSContributionTableEntry entry);

	SSSContributionTable getSSSContributionTable();

	void delete(SSSContributionTableEntry entry);
	
}
