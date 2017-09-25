package com.pj.hrapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SSSContributionTable {

	public static final BigDecimal MINIMUM_COMPENSATION = new BigDecimal("1000");
	
	private List<SSSContributionTableEntry> entries = new ArrayList<>();
	
	public SSSContributionTable(List<SSSContributionTableEntry> entries) {
		this.entries = entries;
	}

	public boolean isComplete() {
		sortEntriesByCompensationFrom();
		
		BigDecimal reference = MINIMUM_COMPENSATION;
		for (int i = 0; i < entries.size(); i++) {
			SSSContributionTableEntry entry = entries.get(i);
			if (entry.getCompensationFrom().compareTo(reference) == 0) {
				if (entry.isCompensationToSpecified()) {
					reference = entry.getCompensationTo().add(new BigDecimal("0.01"));
				} else {
					return i == entries.size() - 1; // true if entry is last
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	private void sortEntriesByCompensationFrom() {
		Collections.sort(entries, (o1, o2) -> o1.getCompensationFrom().compareTo(o2.getCompensationFrom()));
	}

	public List<SSSContributionTableEntry> getEntries() {
		return entries;
	}

	public boolean isValidEntry(SSSContributionTableEntry other) {
		sortEntriesByCompensationFrom();
		
		for (SSSContributionTableEntry entry : entries) {
			if (entry.overlapsWith(other)) {
				return false;
			}
		}
		return true;
	}

	public BigDecimal getEmployeeContribution(BigDecimal compensation) {
		return entries.stream()
			.filter(entry -> entry.contains(compensation))
			.findFirst()
			.get().getEmployeeContribution();
	}
	
}
