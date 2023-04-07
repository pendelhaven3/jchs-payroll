package com.jchs.payrollapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class EmployeeLoanType {

	@Id
	@GeneratedValue
	private Long id;
	
	private String description;
	
	public String toString() {
		return description;
	}
	
}
