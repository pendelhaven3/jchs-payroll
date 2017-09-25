package com.pj.hrapp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.SSSContributionTableEntryDao;
import com.pj.hrapp.model.SSSContributionTableEntry;

@Repository
public class SSSContributionTableEntryDaoImpl implements SSSContributionTableEntryDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public SSSContributionTableEntry get(long id) {
		return entityManager.find(SSSContributionTableEntry.class, id);
	}


	@Override
	public void save(SSSContributionTableEntry entry) {
		if (entry.getId() == null) {
			entityManager.persist(entry);
		} else {
			entityManager.merge(entry);
		}
	}


	@Override
	public List<SSSContributionTableEntry> getAll() {
		return entityManager.createQuery("select e from SSSContributionTableEntry e order by e.compensationFrom",
				SSSContributionTableEntry.class).getResultList();
	}


	@Override
	public void delete(SSSContributionTableEntry entry) {
		entityManager.remove(get(entry.getId()));
	}

}
