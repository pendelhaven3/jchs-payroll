package com.jchs.payrollapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jchs.payrollapp.model.CompanyProfile;

public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

}
