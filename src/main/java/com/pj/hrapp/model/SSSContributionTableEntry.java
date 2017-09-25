package com.pj.hrapp.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.pj.hrapp.model.report.SSSPhilHealthReportItem;
import com.pj.hrapp.model.util.AmountInterval;
import com.pj.hrapp.util.FormatterUtil;

// TODO: Find better place for this!
@SqlResultSetMapping(name = "sssPhilHealthReportItemMapping",
	classes = {
	    @ConstructorResult(targetClass = SSSPhilHealthReportItem.class, columns = {
	        @ColumnResult(name = "employeeFullName"),
	        @ColumnResult(name = "employeeNickname"),
	        @ColumnResult(name = "sssEmployeeContribution"),
	        @ColumnResult(name = "sssEmployerContribution"),
	        @ColumnResult(name = "monthlyPay"),
	        @ColumnResult(name = "pagibigContribution"),
            @ColumnResult(name = "sssNumber"),
            @ColumnResult(name = "philHealthNumber")
	    })
	}
)

@Entity
public class SSSContributionTableEntry {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	private BigDecimal compensationFrom;
	
	private BigDecimal compensationTo;
	private BigDecimal employerContribution;
	private BigDecimal employeeContribution;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AmountInterval getCompensationRange() {
		return new AmountInterval(compensationFrom, compensationTo);
	}
	
	public BigDecimal getCompensationFrom() {
		return compensationFrom;
	}

	public void setCompensationFrom(BigDecimal compensationFrom) {
		this.compensationFrom = compensationFrom;
	}

	public BigDecimal getCompensationTo() {
		return compensationTo;
	}

	public void setCompensationTo(BigDecimal compensationTo) {
		this.compensationTo = compensationTo;
	}

	public BigDecimal getEmployerContribution() {
		return employerContribution;
	}

	public void setEmployerContribution(BigDecimal employerContribution) {
		this.employerContribution = employerContribution;
	}

	public BigDecimal getEmployeeContribution() {
		return employeeContribution;
	}

	public void setEmployeeContribution(BigDecimal employeeContribution) {
		this.employeeContribution = employeeContribution;
	}

	public boolean isCompensationToSpecified() {
		return compensationTo != null;
	}

	public boolean overlapsWith(SSSContributionTableEntry other) {
		if (equals(other)) {
			return false;
		} else {
			return getCompensationRange().overlapsWith(other.getCompensationRange());
		}
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
		SSSContributionTableEntry other = (SSSContributionTableEntry) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}
	
	public String getCompensationRangeAsString() {
		return new StringBuilder()
				.append(FormatterUtil.formatAmount(compensationFrom))
				.append(" - ")
				.append(compensationTo != null ? FormatterUtil.formatAmount(compensationTo) : "over")
				.toString();
	}
	
	public boolean contains(BigDecimal compensation) {
		return compensationFrom.compareTo(compensation) <= 0 &&
				(compensationTo == null || compensation.compareTo(compensationTo) <= 0);
	}
	
}
