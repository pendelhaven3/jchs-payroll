package com.jchs.payrollapp.model;

public enum PaySchedule {

	SEMIMONTHLY("Semimonthly"), WEEKLY("Weekly");

	private String description;
	
	private PaySchedule(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
