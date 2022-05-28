package com.jchs.payrollapp.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jchs.payrollapp.dao.PhilHealthContributionTableRepository;
import com.jchs.payrollapp.model.PhilHealthContributionTable;
import com.jchs.payrollapp.service.PhilHealthService;

@Service
public class PhilHealthServiceImpl implements PhilHealthService {

    @Autowired
    private PhilHealthContributionTableRepository repository;
    
    @Transactional
	@Override
	public void save(PhilHealthContributionTable table) {
	    repository.save(table);
	}

	@Override
	public PhilHealthContributionTable getContributionTable() {
	    return repository.findOne(1L);
	}

}
