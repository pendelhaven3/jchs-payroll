package com.pj.hrapp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.PayslipAdjustmentDao;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PayslipAdjustment;
import com.pj.hrapp.model.PayslipAdjustmentType;
import com.pj.hrapp.model.search.PayslipAdjustmentSearchCriteria;

@Repository
public class PayslipAdjustmentDaoImpl implements PayslipAdjustmentDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void save(PayslipAdjustment payslipAdjustment) {
		if (payslipAdjustment.getId() == null) {
			entityManager.persist(payslipAdjustment);
		} else {
			entityManager.merge(payslipAdjustment);
		}
	}

	@Override
	public List<PayslipAdjustment> findAllByPayslip(Payslip payslip) {
		TypedQuery<PayslipAdjustment> query = entityManager.createQuery(
				"select pa from PayslipAdjustment pa where pa.payslip = :payslip", PayslipAdjustment.class);
		query.setParameter("payslip", payslip);
		return query.getResultList();
	}

	@Override
	public void delete(PayslipAdjustment payslipAdjustment) {
		entityManager.remove(get(payslipAdjustment.getId()));
	}
	
	@Override
	public PayslipAdjustment get(long id) {
		return entityManager.find(PayslipAdjustment.class, id);
	}

	@Override
	public void deleteByPayslipAndType(Payslip payslip, PayslipAdjustmentType type) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<PayslipAdjustment> criteria = criteriaBuilder.createCriteriaDelete(PayslipAdjustment.class);
		Root<PayslipAdjustment> root = criteria.from(PayslipAdjustment.class);
		
		criteria.where(
				criteriaBuilder.equal(root.get("payslip"), payslip),
				criteriaBuilder.equal(root.get("type"), type)
		);
		
		entityManager.createQuery(criteria).executeUpdate();
	}

    @Override
    public List<PayslipAdjustment> search(PayslipAdjustmentSearchCriteria criteria) {
        Map<String, Object> paramMap = new HashMap<>();
        
        StringBuilder sql = new StringBuilder("select pa from PayslipAdjustment pa where 1 = 1");
        if (criteria.getEmployee() != null) {
            sql.append(" and pa.payslip.employee = :employee");
            paramMap.put("employee", criteria.getEmployee());
        }
        if (criteria.getType() != null) {
            sql.append(" and pa.type = :type");
            paramMap.put("type", criteria.getType());
        }
        if (!StringUtils.isEmpty(criteria.getContributionMonth())) {
            sql.append(" and pa.contributionMonth = :contributionMonth");
            paramMap.put("contributionMonth", criteria.getContributionMonth());
        }

        TypedQuery<PayslipAdjustment> query = entityManager.createQuery(sql.toString(), PayslipAdjustment.class);
        for (String key : paramMap.keySet()) {
            query.setParameter(key, paramMap.get(key));
        }
        return query.getResultList();
    }
	
}
