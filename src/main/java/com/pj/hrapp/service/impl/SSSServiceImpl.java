package com.pj.hrapp.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.SSSContributionTableEntryDao;
import com.pj.hrapp.model.SSSContributionTable;
import com.pj.hrapp.model.SSSContributionTableEntry;
import com.pj.hrapp.service.SSSService;

@Service
public class SSSServiceImpl implements SSSService {

	@Autowired private SSSContributionTableEntryDao sssContributonTableEntryDao;
	
	@Override
	public SSSContributionTableEntry getSSSContributionTableEntry(long id) {
		return sssContributonTableEntryDao.get(id);
	}

	@Transactional
	@Override
	public void save(SSSContributionTableEntry entry) {
		sssContributonTableEntryDao.save(entry);
	}

	@Override
	public SSSContributionTable getSSSContributionTable() {
		return new SSSContributionTable(sssContributonTableEntryDao.getAll());
	}

	@Transactional
	@Override
	public void delete(SSSContributionTableEntry entry) {
		sssContributonTableEntryDao.delete(entry);
	}

}
