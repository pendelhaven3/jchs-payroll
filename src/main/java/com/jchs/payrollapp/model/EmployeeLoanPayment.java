package com.jchs.payrollapp.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class EmployeeLoanPayment {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private EmployeeLoan loan;

	@ManyToOne
	private Payslip payslip;

	private Date paymentDate;
	private BigDecimal amount;
	private Integer paymentNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmployeeLoan getLoan() {
		return loan;
	}

	public void setLoan(EmployeeLoan loan) {
		this.loan = loan;
	}

	public Payslip getPayslip() {
		return payslip;
	}

	public void setPayslip(Payslip payslip) {
		this.payslip = payslip;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(Integer paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
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
		EmployeeLoanPayment other = (EmployeeLoanPayment) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}
	
	public String getPaymentNumberDescription() {
		return new StringBuilder()
				.append(paymentNumber)
				.append("x")
				.append(loan.getNumberOfPayments())
				.toString();
	}

	public String getDescription() {
		return new StringBuilder()
				.append(loan.getDescription())
				.append(" (").append(getPaymentNumberDescription()).append(")")
				.toString();
	}
	
	public boolean isLast() {
		return paymentNumber.equals(loan.getNumberOfPayments());
	}
	
}
