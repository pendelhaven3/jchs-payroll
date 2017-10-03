package com.jchs.payrollapp.service.impl;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jchs.payrollapp.dao.CompanyProfileRepository;
import com.jchs.payrollapp.dao.SystemParameterRepository;
import com.jchs.payrollapp.model.CompanyProfile;
import com.jchs.payrollapp.model.SystemParameter;
import com.jchs.payrollapp.service.SystemService;
import com.jchs.payrollapp.util.NumberUtil;

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
//			companyProfile = new CompanyProfile(1L);
            companyProfile = new CompanyProfile();
            companyProfile.setId(1L);
		}
		return companyProfile;
	}

	@Transactional
	@Override
	public void save(CompanyProfile companyProfile) {
		companyProfileRepository.save(companyProfile);
	}

}
