package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.pj.hrapp.model.util.DateInterval;

@Entity
public class Payroll {

	@Id
	@GeneratedValue
	private Long id;
	
	private Long batchNumber;
	
	@Column(columnDefinition = "date")
	private Date payDate;
	
	@Column(columnDefinition = "date null")
	private Date periodCoveredFrom;
	
	@Column(columnDefinition = "date null")
	private Date periodCoveredTo;
	
	@Enumerated(EnumType.STRING)
	private PaySchedule paySchedule;
	
	@OneToMany(mappedBy = "payroll", cascade = CascadeType.REMOVE)
	private List<Payslip> payslips = new ArrayList<>();
	
	@Column(columnDefinition = "boolean default false")
	private boolean posted;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public List<Payslip> getPayslips() {
		return payslips;
	}

	public void setPayslips(List<Payslip> payslips) {
		this.payslips = payslips;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payroll other = (Payroll) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public PaySchedule getPaySchedule() {
		return paySchedule;
	}

	public void setPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
	}

	public DateInterval getPeriodCovered() {
		return new DateInterval(periodCoveredFrom, periodCoveredTo);
	}

	public static Payroll withId(long id) {
		Payroll payroll = new Payroll();
		payroll.setId(id);
		
		return payroll;
	}
	
	public BigDecimal getTotalAmount() {
		return payslips.stream()
				.map(payslip -> payslip.getNetPay())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
	}

	public boolean isPosted() {
		return posted;
	}

	public void setPosted(boolean posted) {
		this.posted = posted;
	}

	public Date getPeriodCoveredFrom() {
		return periodCoveredFrom;
	}

	public void setPeriodCoveredFrom(Date periodCoveredFrom) {
		this.periodCoveredFrom = periodCoveredFrom;
	}

	public Date getPeriodCoveredTo() {
		return periodCoveredTo;
	}

	public void setPeriodCoveredTo(Date periodCoveredTo) {
		this.periodCoveredTo = periodCoveredTo;
	}
	
}
