package com.pj.hrapp.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.service.SalaryService;

@Service
public class SalaryServiceImpl implements SalaryService {

	@Autowired private SalaryDao salaryDao;
	
	@Override
	public List<Salary> getAllCurrentSalaries() {
		return salaryDao.getAllCurrent();
	}

	@Transactional
	@Override
	public void save(Salary salary) {
		salaryDao.save(salary);
	}

	@Override
	public Salary getSalary(long id) {
		return salaryDao.get(id);
	}

	@Override
	public Salary findSalaryByEmployeeAndEffectiveDate(Employee employee, Date effectiveDate) {
		return salaryDao.findByEmployeeAndEffectiveDate(employee, effectiveDate);
	}

	@Transactional
	@Override
	public void delete(Salary salary) {
		salaryDao.delete(salary);
	}

	@Override
	public List<Salary> searchSalaries(SalarySearchCriteria criteria) {
		return salaryDao.search(criteria);
	}
	
}
