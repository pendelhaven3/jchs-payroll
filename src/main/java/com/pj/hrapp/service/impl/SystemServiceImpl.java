package com.pj.hrapp.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.CompanyProfileRepository;
import com.pj.hrapp.dao.SystemParameterRepository;
import com.pj.hrapp.model.CompanyProfile;
import com.pj.hrapp.model.SystemParameter;
import com.pj.hrapp.service.SystemService;
import com.pj.hrapp.util.NumberUtil;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired private SystemParameterRepository systemParameterRepository;
	@Autowired private CompanyProfileRepository companyProfileRepository;
	
	@Override
	public BigDecimal getPagibigContributionValue() {
		return NumberUtil.toBigDecimal(
				systemParameterRepository.findByName(SystemParameter.PAGIBIG_CONTRIBUTION_VALUE_PARAMETER_NAME)
				.getValue());
	}

	@Transactional
	@Override
	public void updatePagibigContributionValue(BigDecimal newValue) {
		SystemParameter systemParameter = systemParameterRepository.findByName(
				SystemParameter.PAGIBIG_CONTRIBUTION_VALUE_PARAMETER_NAME);
		systemParameter.setValue(newValue.toString());
		systemParameterRepository.save(systemParameter);
	}

	@Override
	public CompanyProfile getCompanyProfile() {
		CompanyProfile companyProfile = companyProfileRepository.findOne(1L);
		if (companyProfile == null) {
			companyProfile = new CompanyProfile(1L);
		}
		return companyProfile;
	}

	@Transactional
	@Override
	public void save(CompanyProfile companyProfile) {
		companyProfileRepository.save(companyProfile);
	}

}
