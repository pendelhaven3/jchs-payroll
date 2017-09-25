package com.pj.hrapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SystemParameter {

	public static final String PAGIBIG_CONTRIBUTION_VALUE_PARAMETER_NAME = "PAGIBIG_CONTRIBUTION_VALUE";
	
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
