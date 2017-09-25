package com.pj.hrapp.dao.impl;

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

import com.pj.hrapp.dao.ReportDao;
import com.pj.hrapp.model.report.LatesReportItem;
import com.pj.hrapp.model.report.SSSPhilHealthReportItem;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.Queries;

@Repository
public class ReportDaoImpl implements ReportDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SSSPhilHealthReportItem> getSSSPhilHealthReportItems(YearMonth yearMonth) {
		Query query = entityManager.createNativeQuery(
				Queries.getQuery("sssPhilHealthReport"), "sssPhilHealthReportItemMapping");
		query.setParameter("month", yearMonth.getMonth().getValue());
		query.setParameter("year", yearMonth.getYear());
		query.setParameter("firstDayOfMonth", DateUtil.toDate(yearMonth.atDay(1)));
		query.setParameter("numberOfWorkingDaysInFirstHalf", DateUtil.getNumberOfWorkingDaysInFirstHalf(yearMonth));
		query.setParameter("numberOfWorkingDaysInSecondHalf", DateUtil.getNumberOfWorkingDaysInSecondHalf(yearMonth));
		query.setParameter("contributionMonth", DateUtil.toString(yearMonth));
		return query.getResultList();
	}

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

}
