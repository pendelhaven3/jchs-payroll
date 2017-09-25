package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayrollDao;
import com.pj.hrapp.model.Payroll;

@Repository
public class PayrollDaoImpl implements PayrollDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Payroll> getAll() {
		return entityManager.createQuery("select p from Payroll p order by p.payDate desc",
				Payroll.class).getResultList();
	}

	@Override
	public void save(Payroll payroll) {
		if (payroll.getId() == null) {
			entityManager.persist(payroll);
		} else {
			entityManager.merge(payroll);
		}
	}

	@Override
	public Payroll get(long id) {
		return entityManager.find(Payroll.class, id);
	}

	@Override
	public Payroll findByBatchNumber(long batchNumber) {
		TypedQuery<Payroll> query = entityManager.createQuery(
				"select p from Payroll p where p.batchNumber = :batchNumber",
				Payroll.class);
		query.setParameter("batchNumber", batchNumber);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void delete(Payroll payroll) {
		entityManager.remove(get(payroll.getId()));
	}

	@Override
	public long getLatestBatchNumber() {
		return entityManager.createQuery("SELECT COALESCE(MAX(p.batchNumber), 0) + 1 FROM Payroll p", Long.class)
			.getSingleResult();
	}

}
