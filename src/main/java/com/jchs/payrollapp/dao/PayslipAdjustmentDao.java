package com.jchs.payrollapp.dao;

import java.util.List;

import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.PayslipAdjustment;
import com.jchs.payrollapp.model.PayslipAdjustmentType;
import com.jchs.payrollapp.model.search.PayslipAdjustmentSearchCriteria;

public interface PayslipAdjustmentDao {

	void save(PayslipAdjustment payslipAdjustment);

	List<PayslipAdjustment> findAllByPayslip(Payslip payslip);

	void delete(PayslipAdjustment payslipAdjustment);

	PayslipAdjustment get(long id);

	void deleteByPayslipAndType(Payslip payslip, PayslipAdjustmentType sss);

    List<PayslipAdjustment> search(PayslipAdjustmentSearchCriteria criteria);
	
}
