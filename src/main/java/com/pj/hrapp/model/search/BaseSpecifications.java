package com.pj.hrapp.model.search;

import org.springframework.data.jpa.domain.Specifications;

public class BaseSpecifications {

	public static <T> Specifications<T> build() {
		return Specifications.where((root, query, builder) -> builder.conjunction());
	}
	
}
