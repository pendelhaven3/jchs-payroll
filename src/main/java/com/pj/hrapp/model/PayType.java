package com.pj.hrapp.model;

public enum PayType {

	PER_DAY("Per Day"), FIXED_RATE("Fixed Rate");
	
	private String description;
	
	private PayType(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return description;
	}
	
}
