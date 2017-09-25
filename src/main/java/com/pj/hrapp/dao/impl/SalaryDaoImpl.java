package com.pj.hrapp.dao.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.SalaryDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.SalarySearchCriteria;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.Queries;

@Repository
public class SalaryDaoImpl implements SalaryDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(Salary salary) {
		if (salary.getId() == null) {
			entityManager.persist(salary);
		} else {
			entityManager.merge(salary);
		}
	}

	@Override
	public Salary get(long id) {
		return entityManager.find(Salary.class, id);
	}

	@Override
	public List<Salary> getAllCurrent() {
		return entityManager.createQuery(
				"select s from Salary s where s.effectiveDateTo is null and s.employee.resigned = false"
				+ " order by s.employee.lastName, s.employee.firstName", Salary.class)
				.getResultList();
	}

	@Override
	public Salary findByEmployeeAndEffectiveDate(Employee employee, Date effectiveDate) {
		TypedQuery<Salary> query = entityManager.createQuery(
				"select s from Salary s where s.employee = :employee and s.effectiveDateFrom <= :effectiveDate"
				+ " and (s.effectiveDateTo is null or s.effectiveDateTo > :effectiveDate)", Salary.class);
		query.setParameter("employee", employee);
		query.setParameter("effectiveDate", effectiveDate);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void delete(Salary salary) {
		entityManager.remove(get(salary.getId()));
	}

	@Override
	public List<Salary> search(SalarySearchCriteria criteria) {
		Map<String, Object> paramMap = new HashMap<>();
		
		StringBuilder sql = new StringBuilder("select s from Salary s where 1 = 1");
		if (criteria.getEmployee() != null) {
			sql.append(" and s.employee = :employee");
			paramMap.put("employee", criteria.getEmployee());
		}
		if (criteria.getEffectiveDateFrom() != null) {
			sql.append(" and (")
				.append("s.effectiveDateFrom >= :effectiveDateFrom")
				.append(" or (s.effectiveDateFrom <= :effectiveDateFrom and (s.effectiveDateTo is null or s.effectiveDateTo >= :effectiveDateFrom))")
				.append(")");
			paramMap.put("effectiveDateFrom", criteria.getEffectiveDateFrom());
		}
		if (criteria.getEffectiveDateTo() != null) {
			sql.append(" and (")
				.append("s.effectiveDateTo <= :effectiveDateTo")
				.append(" or (s.effectiveDateFrom <= :effectiveDateTo and (s.effectiveDateTo is null or s.effectiveDateTo >= :effectiveDateTo))")
				.append(")");
			paramMap.put("effectiveDateTo", criteria.getEffectiveDateTo());
		}
		if (criteria.getPaySchedule() != null) {
			sql.append(" and s.employee.paySchedule = :paySchedule");
			paramMap.put("paySchedule", criteria.getPaySchedule());
		}

		TypedQuery<Salary> query = entityManager.createQuery(sql.toString(), Salary.class);
		for (String key : paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		return query.getResultList();
	}

	@Override
	public Salary findByEmployee(Employee employee) {
		TypedQuery<Salary> query = 
				entityManager.createQuery("select s from Salary s where s.employee = :employee", Salary.class);
		query.setParameter("employee", employee);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public BigDecimal getEmployeeCompensationForMonthYear(Employee employee, YearMonth yearMonth) {
		Query query = entityManager.createNativeQuery(Queries.getQuery("monthlyPay"));
		query.setParameter("employeeId", employee.getId());
		query.setParameter("month", yearMonth.getMonth().getValue());
		query.setParameter("year", yearMonth.getYear());
		query.setParameter("firstDayOfMonth", DateUtil.toDate(yearMonth.atDay(1)));
		query.setParameter("numberOfWorkingDaysInFirstHalf", DateUtil.getNumberOfWorkingDaysInFirstHalf(yearMonth));
		query.setParameter("numberOfWorkingDaysInSecondHalf", DateUtil.getNumberOfWorkingDaysInSecondHalf(yearMonth));
		
		try {
			return (BigDecimal)query.getSingleResult();
		} catch (NoResultException e) {
			return BigDecimal.ZERO;
		}
	}
	
}
