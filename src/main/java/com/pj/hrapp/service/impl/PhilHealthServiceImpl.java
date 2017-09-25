package com.pj.hrapp.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.PhilHealthContributionTableEntryDao;
import com.pj.hrapp.model.PhilHealthContributionTable;
import com.pj.hrapp.model.PhilHealthContributionTableEntry;
import com.pj.hrapp.service.PhilHealthService;

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
