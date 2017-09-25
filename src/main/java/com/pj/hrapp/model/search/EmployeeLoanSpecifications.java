package com.pj.hrapp.model.search;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.EmployeeLoanType;

public class EmployeeLoanSpecifications extends BaseSpecifications {

	public static Specification<EmployeeLoan> withEmployee(Employee employee) {
		return (root, query, builder) -> builder.equal(root.get("employee"), employee);
	}
	
	public static Specification<EmployeeLoan> withLoanType(EmployeeLoanType loanType) {
		switch (loanType) {
		case COMPANY:
			return (root, query, builder) -> builder.equal(builder.upper(root.get("description")), "CO LOAN");
		case SSS:
			return (root, query, builder) -> builder.equal(builder.upper(root.get("description")), "SSS LOAN");
		case PAGIBIG:
			return (root, query, builder) -> builder.equal(builder.upper(root.get("description")), "PAGIBIG LOAN");
		default:
			return null;
		}
	}

	public static Specification<EmployeeLoan> withPaid(Boolean paid) {
		return (root, query, builder) -> builder.equal(root.get("paid"), paid);
	}

	public static Specification<EmployeeLoan> withPaymentDate(Date paymentDate) {
		return (root, query, builder) -> builder.or(
				builder.isNull(root.get("paymentStartDate")),
				builder.lessThanOrEqualTo(root.get("paymentStartDate"), paymentDate));
	}
	
}
