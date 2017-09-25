package com.pj.hrapp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayslipDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.search.PayslipSearchCriteria;

@Repository
public class PayslipDaoImpl implements PayslipDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void deleteAllByPayroll(Payroll payroll) {
		Payroll updated = entityManager.find(Payroll.class, payroll.getId());
		updated.getPayslips().clear();
		
		entityManager.merge(updated);
		
		Query query = entityManager.createQuery("delete from Payslip p where p.payroll = :payroll");
		query.setParameter("payroll", payroll);
		query.executeUpdate();
	}

	@Override
	public void save(Payslip payslip) {
		if (payslip.getId() == null) {
			entityManager.persist(payslip);
		} else {
			entityManager.merge(payslip);
		}
	}

	@Override
	public List<Payslip> findAllByPayroll(Payroll payroll) {
		TypedQuery<Payslip> query = entityManager.createQuery(
				"select p from Payslip p where p.payroll = :payroll"
				+ " order by p.employee.lastName, p.employee.firstName", Payslip.class);
		query.setParameter("payroll", payroll);
		return query.getResultList();
	}

	@Override
	public Payslip get(long id) {
		return entityManager.find(Payslip.class, id);
	}

	@Override
	public void delete(Payslip payslip) {
		entityManager.remove(get(payslip.getId()));
	}

	@Override
	public List<Payslip> search(PayslipSearchCriteria criteria) {
		Map<String, Object> paramMap = new HashMap<>();
		
		StringBuilder sql = new StringBuilder("select p from Payslip p where 1 = 1");
		if (criteria.getEmployee() != null) {
			sql.append(" and p.employee = :employee");
			paramMap.put("employee", criteria.getEmployee());
		}
		if (criteria.getPayDateLessThan() != null) {
			sql.append(" and p.payroll.payDate < :payDate");
			paramMap.put("payDate", criteria.getPayDateLessThan());
		}
		sql.append(" order by p.payroll.payDate desc");
		
		TypedQuery<Payslip> query = entityManager.createQuery(sql.toString(), Payslip.class);
		for (String key : paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		return query.getResultList();
	}

	@Override
	public Payslip findAnyPayslipByEmployee(Employee employee) {
		TypedQuery<Payslip> query = entityManager.createQuery("select p from Payslip p where p.employee = :employee",
				Payslip.class);
		query.setParameter("employee", employee);
		query.setMaxResults(1);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
