package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.pj.hrapp.util.FormatterUtil;

@Entity
public class ValeProduct {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Payslip payslip;
	
	private Long salesInvoiceNumber;
	
	private Date transactionDate;
	
	private BigDecimal amount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSalesInvoiceNumber() {
		return salesInvoiceNumber;
	}

	public void setSalesInvoiceNumber(Long salesInvoiceNumber) {
		this.salesInvoiceNumber = salesInvoiceNumber;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setPayslip(Payslip payslip) {
		this.payslip = payslip;
	}

	public String getDescription() {
		return new StringBuilder("vp ")
				.append(FormatterUtil.formatDateMonth(transactionDate))
				.append(" #")
				.append(salesInvoiceNumber.toString())
				.toString();
	}
	
}
