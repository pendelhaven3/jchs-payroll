package com.jchs.payrollapp.dao.impl;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.jchs.payrollapp.dao.ReportDao;
import com.jchs.payrollapp.model.report.LatesReportItem;
import com.jchs.payrollapp.model.report.PagIbigReportItem;
import com.jchs.payrollapp.model.report.PhilHealthReportItem;
import com.jchs.payrollapp.model.report.SSSReportItem;
import com.jchs.payrollapp.util.DateUtil;
import com.jchs.payrollapp.util.Queries;

@Repository
public class ReportDaoImpl implements ReportDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LatesReportItem> getLatesReportItems(Date dateFrom, Date dateTo) {
		final Query query = entityManager.createNativeQuery(Queries.getQuery("latesReport"));
		query.setParameter("dateFrom", dateFrom);
		query.setParameter("dateTo", dateTo);
		
		query.unwrap(SQLQuery.class)
			.addScalar("employeeNickname", StringType.INSTANCE)
			.addScalar("lates", IntegerType.INSTANCE)
			.setResultTransformer(Transformers.aliasToBean(LatesReportItem.class));
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PhilHealthReportItem> getPhilHealthReportItems(YearMonth yearMonth) {
        Query query = entityManager.createNativeQuery(
                Queries.getQuery("philHealthReport"), "philHealthReportItemMapping");
        query.setParameter("firstDayOfMonth", DateUtil.toDate(yearMonth.atDay(1)));
        query.setParameter("numberOfWorkingDaysInFirstHalf", DateUtil.getNumberOfWorkingDaysInFirstHalf(yearMonth));
        query.setParameter("numberOfWorkingDaysInSecondHalf", DateUtil.getNumberOfWorkingDaysInSecondHalf(yearMonth));
        query.setParameter("contributionMonth", DateUtil.toString(yearMonth));
        return (List<PhilHealthReportItem>)query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SSSReportItem> getSSSReportItems(YearMonth yearMonth) {
        Query query = entityManager.createNativeQuery(
                Queries.getQuery("sssReport"), "sssReportItemMapping");
        query.setParameter("firstDayOfMonth", DateUtil.toDate(yearMonth.atDay(1)));
        query.setParameter("numberOfWorkingDaysInFirstHalf", DateUtil.getNumberOfWorkingDaysInFirstHalf(yearMonth));
        query.setParameter("numberOfWorkingDaysInSecondHalf", DateUtil.getNumberOfWorkingDaysInSecondHalf(yearMonth));
        query.setParameter("contributionMonth", DateUtil.toString(yearMonth));
        return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PagIbigReportItem> getPagIbigReportItems(YearMonth yearMonth) {
        Query query = entityManager.createNativeQuery(
                Queries.getQuery("pagIbigReport"), "pagIbigReportItemMapping");
        query.setParameter("firstDayOfMonth", DateUtil.toDate(yearMonth.atDay(1)));
        query.setParameter("contributionMonth", DateUtil.toString(yearMonth));
        return query.getResultList();
	}

}
