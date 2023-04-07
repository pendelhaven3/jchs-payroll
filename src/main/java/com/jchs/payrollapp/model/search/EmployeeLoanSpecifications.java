package com.jchs.payrollapp.model.search;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeLoan;
import com.jchs.payrollapp.model.EmployeeLoanType;

public class EmployeeLoanSpecifications extends BaseSpecifications {

	public static Specification<EmployeeLoan> withEmployee(Employee employee) {
		return (root, query, builder) -> builder.equal(root.get("employee"), employee);
	}
	
	public static Specification<EmployeeLoan> withLoanType(EmployeeLoanType loanType) {
		return (root, query, builder) -> builder.equal(root.get("loanType"), loanType);
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
