package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PhilHealthContributionTableEntryDao;
import com.pj.hrapp.model.PhilHealthContributionTableEntry;

@Repository
public class PhilHealthContributionTableEntryDaoImpl implements PhilHealthContributionTableEntryDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public PhilHealthContributionTableEntry get(long id) {
		return entityManager.find(PhilHealthContributionTableEntry.class, id);
	}


	@Override
	public void save(PhilHealthContributionTableEntry entry) {
		if (entry.getId() == null) {
			entityManager.persist(entry);
		} else {
			entityManager.merge(entry);
		}
	}


	@Override
	public List<PhilHealthContributionTableEntry> getAll() {
		return entityManager.createQuery("select e from PhilHealthContributionTableEntry e order by e.salaryFrom",
				PhilHealthContributionTableEntry.class).getResultList();
	}


	@Override
	public void delete(PhilHealthContributionTableEntry entry) {
		entityManager.remove(get(entry.getId()));
	}

}
