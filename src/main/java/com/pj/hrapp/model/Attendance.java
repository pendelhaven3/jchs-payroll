package com.pj.hrapp.model;

public enum Attendance {

	WHOLE_DAY(1, "Whole Day"), 
	HALF_DAY(0.5, "Half Day"), 
	ABSENT(0, "Absent"),
	HOLIDAY(0, "Holiday");
	
	private String description;
	private double value;
	
	private Attendance(double value, String description) {
		this.value = value;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
	public double getValue() {
		return value;
	}
	
}
 