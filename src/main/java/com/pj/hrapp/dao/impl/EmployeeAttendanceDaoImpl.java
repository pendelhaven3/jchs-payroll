package com.pj.hrapp.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.pj.hrapp.dao.EmployeeAttendanceDao;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;

@Repository
public class EmployeeAttendanceDaoImpl implements EmployeeAttendanceDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<EmployeeAttendance> search(EmployeeAttendanceSearchCriteria criteria) {
		Map<String, Object> paramMap = new HashMap<>();
		
		StringBuilder sql = new StringBuilder("select ea from EmployeeAttendance ea where 1 = 1");
		if (criteria.getEmployee() != null) {
			sql.append(" and ea.employee = :employee");
			paramMap.put("employee", criteria.getEmployee());
		}
		if (criteria.getDateFrom() != null) {
			sql.append(" and ea.date >= :dateFrom");
			paramMap.put("dateFrom", criteria.getDateFrom());
		}
		if (criteria.getDateTo() != null) {
			sql.append(" and ea.date <= :dateTo");
			paramMap.put("dateTo", criteria.getDateTo());
		}
		if (criteria.getPaySchedule() != null) {
			sql.append(" and ea.employee.paySchedule = :paySchedule");
			paramMap.put("paySchedule", criteria.getPaySchedule());
		}

		sql.append(" order by ea.date");
		
		TypedQuery<EmployeeAttendance> query = entityManager.createQuery(sql.toString(), EmployeeAttendance.class);
		for (String key : paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		return query.getResultList();
	}

	@Override
	public void save(EmployeeAttendance attendance) {
		if (attendance.getId() == null) {
			entityManager.persist(attendance);
		} else {
			entityManager.merge(attendance);
		}
	}

	@Override
	public EmployeeAttendance get(long id) {
		return entityManager.find(EmployeeAttendance.class, id);
	}

	@Override
	public EmployeeAttendance findByEmployeeAndDate(Employee employee, Date date) {
		TypedQuery<EmployeeAttendance> query = entityManager.createQuery(
				"select ea from EmployeeAttendance ea where ea.employee = :employee and date = :date",
				EmployeeAttendance.class);
		query.setParameter("employee", employee);
		query.setParameter("date", date);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void delete(EmployeeAttendance employeeAttendance) {
		entityManager.remove(get(employeeAttendance.getId()));
	}

}
