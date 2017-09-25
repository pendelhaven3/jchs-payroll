package com.pj.hrapp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.ValeProduct;

public interface ValeProductRepository extends JpaRepository<ValeProduct, Long> {

	@Query("select v from ValeProduct v where v.payslip = :payslip order by v.transactionDate")
	List<ValeProduct> findAllByPayslip(@Param("payslip") Payslip payslip);
	
}
