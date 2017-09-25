package com.pj.hrapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pj.hrapp.dao.SystemParameterRepository;
import com.pj.hrapp.model.SystemParameter;

@Component
public class SystemSetup {

	@Autowired private SystemParameterRepository systemParameterRepository;
	
	@Transactional
	public void run() {
		if (!isPagibigContributionValueSet()) {
			setInitialPagibigContributionValue();
		}
	}

	private boolean isPagibigContributionValueSet() {
		return systemParameterRepository
				.findByName(SystemParameter.PAGIBIG_CONTRIBUTION_VALUE_PARAMETER_NAME) != null;
	}
	
	private void setInitialPagibigContributionValue() {
//		SystemParameter systemParameter = new SystemParameter();
//		systemParameter.setName(SystemParameter.PAGIBIG_CONTRIBUTION_VALUE_PARAMETER_NAME);
//		systemParameter.setValue("100");
//		systemParameterRepository.save(systemParameter);
	}

}
