package com.pj.hrapp.dao;

import java.util.List;

import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipAdjustmentType;
import com.pj.hrapp.model.search.PayslipAdjustmentSearchCriteria;

public interface PayslipAdjustmentDao {

	void save(PayslipAdjustment payslipAdjustment);

	List<PayslipAdjustment> findAllByPayslip(Payslip payslip);

	void delete(PayslipAdjustment payslipAdjustment);

	PayslipAdjustment get(long id);

	void deleteByPayslipAndType(Payslip payslip, PayslipAdjustmentType sss);

    List<PayslipAdjustment> search(PayslipAdjustmentSearchCriteria criteria);
	
}
