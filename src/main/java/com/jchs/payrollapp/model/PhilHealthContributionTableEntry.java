package com.jchs.payrollapp.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jchs.payrollapp.model.util.AmountInterval;
import com.jchs.payrollapp.util.FormatterUtil;

@Entity
public class PhilHealthContributionTableEntry {

	@Id
	@GeneratedValue
	private Long id;

	private BigDecimal salaryFrom;
	private BigDecimal salaryTo;
	private BigDecimal employeeShare;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AmountInterval getSalaryRange() {
		return new AmountInterval(salaryFrom, salaryTo);
	}

	public boolean overlapsWith(PhilHealthContributionTableEntry other) {
		if (equals(other)) {
			return false;
		} else {
			return getSalaryRange().overlapsWith(other.getSalaryRange());
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhilHealthContributionTableEntry other = (PhilHealthContributionTableEntry) obj;
		return new EqualsBuilder().append(id, other.getId()).isEquals();
	}

	public String getSalaryRangeAsString() {
		if (salaryFrom == null) {
			return new StringBuilder().append(FormatterUtil.formatAmount(salaryTo)).append(" and below").toString();
		} else if (salaryTo == null) {
			return new StringBuilder().append(FormatterUtil.formatAmount(salaryFrom)).append(" and up").toString();
		} else {
			return new StringBuilder().append(FormatterUtil.formatAmount(salaryFrom)).append(" - ")
					.append(FormatterUtil.formatAmount(salaryTo)).toString();
		}
	}

	public boolean contains(BigDecimal compensation) {
		BigDecimal from = (salaryFrom != null) ? salaryFrom : BigDecimal.ZERO;
		BigDecimal to = (salaryTo != null) ? salaryTo : BigDecimal.valueOf(999999999L);
		
		return from.compareTo(compensation) <= 0 && compensation.compareTo(to) <= 0;
	}

	public BigDecimal getSalaryFrom() {
		return salaryFrom;
	}

	public void setSalaryFrom(BigDecimal salaryFrom) {
		this.salaryFrom = salaryFrom;
	}

	public BigDecimal getSalaryTo() {
		return salaryTo;
	}

	public void setSalaryTo(BigDecimal salaryTo) {
		this.salaryTo = salaryTo;
	}

	public BigDecimal getEmployeeShare() {
		return employeeShare;
	}

	public void setEmployeeShare(BigDecimal employeeShare) {
		this.employeeShare = employeeShare;
	}

	public boolean isSalaryToSpecified() {
		return salaryTo != null;
	}

}
