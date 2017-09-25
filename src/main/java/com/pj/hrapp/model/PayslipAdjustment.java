package com.pj.hrapp.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PayslipAdjustment {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Payslip payslip;
	
	@Enumerated(EnumType.STRING)
	private PayslipAdjustmentType type;
	
	private String description;
	private BigDecimal amount;
	private String contributionMonth;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Payslip getPayslip() {
		return payslip;
	}

	public void setPayslip(Payslip payslip) {
		this.payslip = payslip;
	}

	public PayslipAdjustmentType getType() {
		return type;
	}

	public void setType(PayslipAdjustmentType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getContributionMonth() {
		return contributionMonth;
	}

	public void setContributionMonth(String contributionMonth) {
		this.contributionMonth = contributionMonth;
	}

}
