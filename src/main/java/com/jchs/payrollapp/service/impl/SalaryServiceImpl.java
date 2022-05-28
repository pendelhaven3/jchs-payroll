package com.jchs.payrollapp.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jchs.payrollapp.dao.SalaryDao;
import com.jchs.payrollapp.dao.SalaryRepository;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.Salary;
import com.jchs.payrollapp.model.search.SalarySearchCriteria;
import com.jchs.payrollapp.service.SalaryService;

@Service
public class SalaryServiceImpl implements SalaryService {

	@Autowired private SalaryDao salaryDao;
	@Autowired private SalaryRepository salaryRepository;
	
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

    @Override
    public Salary getCurrentSalary(Employee employee, Date currentDate) {
        return salaryRepository.findCurrentSalary(employee, currentDate);
    }
	
}
