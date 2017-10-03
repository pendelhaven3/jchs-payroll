package com.jchs.payrollapp.model.search;

import org.springframework.data.jpa.domain.Specification;

import com.jchs.payrollapp.model.Employee;

public class EmployeeSpecifications extends BaseSpecifications {

	public static Specification<Employee> withLastName(String lastName) {
		return (root, query, builder) -> builder.like(root.get("lastName"), lastName + "%");
	}
	
	public static Specification<Employee> withFirstName(String firstName) {
		return (root, query, builder) -> builder.like(root.get("firstName"), firstName + "%");
	}
	
	public static Specification<Employee> withResigned(Boolean resigned) {
		return (root, query, builder) -> builder.equal(root.get("resigned"), resigned);
	}

}
