package com.jchs.payrollapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jchs.payrollapp.util.FormatterUtil;

@Entity
public class EmployeeLoan {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Employee employee;

	@Column(columnDefinition = "date")
	private Date loanDate;
	
	private String description;
	private BigDecimal amount;
	private Integer numberOfPayments;
	private String remarks;

	@Column(columnDefinition = "boolean default false")
	private boolean paid;
	
	@OneToMany(mappedBy = "loan", cascade = CascadeType.REMOVE)
	private List<EmployeeLoanPayment> payments;
	
	@Column(columnDefinition = "date")
	private Date paymentStartDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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

	public Integer getNumberOfPayments() {
		return numberOfPayments;
	}

	public void setNumberOfPayments(Integer numberOfPayments) {
		this.numberOfPayments = numberOfPayments;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<EmployeeLoanPayment> getPayments() {
		return payments;
	}

	public void setPayments(List<EmployeeLoanPayment> payments) {
		this.payments = payments;
	}

	public Date getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}

	public BigDecimal getPaymentAmount() {
		return amount.divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP);
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
		EmployeeLoan other = (EmployeeLoan) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public Integer getNextPaymentNumber() {
		return payments.stream()
				.map(payment -> payment.getPaymentNumber())
				.max((o1, o2) -> o1.compareTo(o2))
				.orElseGet(() -> 0) + 1;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("Loan [").append(FormatterUtil.formatDate(loanDate)).append("]")
				.toString();
	}
	
	public String getLastPaymentNumberDescription() {
		int lastPaymentNumber = payments.stream()
				.map(payment -> payment.getPaymentNumber())
				.max((o1, o2) -> o1.compareTo(o2))
				.orElseGet(() -> 0);
		
		if (lastPaymentNumber == 0) {
			return "None";
		} else {
			return new StringBuilder().append(lastPaymentNumber).append("x").append(numberOfPayments).toString();
		}
	}
	
	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getPaymentStatus() {
		if (paid) {
			return "Paid";
		} else {
			return getLastPaymentNumberDescription();
		}
	}

	public static EmployeeLoan withId(long id) {
		EmployeeLoan loan = new EmployeeLoan();
		loan.setId(id);
		return loan;
	}

	public boolean isNew() {
		return id == null;
	}

	public Date getPaymentStartDate() {
		return paymentStartDate;
	}

	public void setPaymentStartDate(Date paymentStartDate) {
		this.paymentStartDate = paymentStartDate;
	}

	public BigDecimal getBalance() {
		return amount.subtract(payments.stream()
				.map(payment -> payment.getAmount())
				.reduce(BigDecimal.ZERO, (x,y) -> x.add(y)));
	}
	
}
