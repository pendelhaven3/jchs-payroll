package com.pj.hrapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pj.hrapp.model.CompanyProfile;

public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

}
