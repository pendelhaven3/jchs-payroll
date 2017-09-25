package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhilHealthContributionTable {

	private List<PhilHealthContributionTableEntry> entries = new ArrayList<>();
	
	public PhilHealthContributionTable(List<PhilHealthContributionTableEntry> entries) {
		this.entries = entries;
	}

	public boolean isComplete() {
		sortEntriesBySalaryFrom();

		if (entries.isEmpty()) {
			return false;
		}
		
		BigDecimal reference = BigDecimal.ZERO;
		for (int i = 0; i < entries.size(); i++) {
			PhilHealthContributionTableEntry entry = entries.get(i);
			BigDecimal salaryFrom = entry.getSalaryFrom() != null ? entry.getSalaryFrom() : BigDecimal.ZERO;
			
			if (salaryFrom.compareTo(reference) == 0) {
				if (entry.isSalaryToSpecified()) {
					reference = entry.getSalaryTo().add(new BigDecimal("0.01"));
				} else {
					return i == entries.size() - 1; // true if entry is last
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	private void sortEntriesBySalaryFrom() {
		Collections.sort(entries, (o1, o2) -> {
			if (o1.getSalaryFrom() == null) {
				return -1;
			} else if (o2.getSalaryFrom() == null) {
				return 1;
			} else {
				return o1.getSalaryFrom().compareTo(o2.getSalaryFrom());	
			}
		});
	}

	public List<PhilHealthContributionTableEntry> getEntries() {
		return entries;
	}

	public boolean isValidEntry(PhilHealthContributionTableEntry other) {
		sortEntriesBySalaryFrom();
		
		for (PhilHealthContributionTableEntry entry : entries) {
			if (entry.overlapsWith(other)) {
				return false;
			}
		}
		return true;
	}

	public BigDecimal getEmployeeShare(BigDecimal salary) {
		return entries.stream()
			.filter(entry -> entry.contains(salary))
			.findFirst()
			.get().getEmployeeShare();
	}
	
}
