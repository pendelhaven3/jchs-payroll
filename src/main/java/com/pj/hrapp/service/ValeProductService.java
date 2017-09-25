package com.pj.hrapp.service;

import java.util.List;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.ValeProduct;

public interface ValeProductService {

	List<ValeProduct> findUnpaidValeProductsByEmployee(Employee employee);

	void addValeProductsToPayslip(List<ValeProduct> valeProducts, Payslip payslip);
	
	void markValeProductsAsPaid(List<ValeProduct> valeProducts);
	
}
