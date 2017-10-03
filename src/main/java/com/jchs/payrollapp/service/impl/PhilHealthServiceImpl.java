package com.jchs.payrollapp.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jchs.payrollapp.dao.PhilHealthContributionTableEntryDao;
import com.jchs.payrollapp.model.PhilHealthContributionTable;
import com.jchs.payrollapp.model.PhilHealthContributionTableEntry;
import com.jchs.payrollapp.service.PhilHealthService;

@Service
public class PhilHealthServiceImpl implements PhilHealthService {

	@Autowired private PhilHealthContributionTableEntryDao philHealthContributonTableEntryDao;
	
	@Override
	public PhilHealthContributionTableEntry getPhilHealthContributionTableEntry(long id) {
		return philHealthContributonTableEntryDao.get(id);
	}

	@Transactional
	@Override
	public void save(PhilHealthContributionTableEntry entry) {
		philHealthContributonTableEntryDao.save(entry);
	}

	@Override
	public PhilHealthContributionTable getContributionTable() {
		return new PhilHealthContributionTable(philHealthContributonTableEntryDao.getAll());
	}

	@Transactional
	@Override
	public void delete(PhilHealthContributionTableEntry entry) {
		philHealthContributonTableEntryDao.delete(entry);
	}

}
