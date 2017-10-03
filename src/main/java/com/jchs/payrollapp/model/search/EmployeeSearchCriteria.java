package com.jchs.payrollapp.model.search;

public class EmployeeSearchCriteria {

	private String lastName;
	private String firstName;
	private Boolean resigned;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Boolean getResigned() {
		return resigned;
	}

	public void setResigned(Boolean resigned) {
		this.resigned = resigned;
	}

}
