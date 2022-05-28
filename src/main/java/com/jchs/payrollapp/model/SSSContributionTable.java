package com.jchs.payrollapp.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.jchs.payrollapp.exception.NoMatchingSssContributionTableEntryException;
import com.jchs.payrollapp.util.FormatterUtil;

import lombok.Getter;

@Getter
public class SSSContributionTable {

	public static final BigDecimal MINIMUM_COMPENSATION = new BigDecimal("1000");
	
	private List<SSSContributionTableEntry> entries = new ArrayList<>();
    private List<SSSContributionTableEntry> householdEntries = new ArrayList<>();
	
	public SSSContributionTable(List<SSSContributionTableEntry> allEntries) {
	    for (SSSContributionTableEntry entry : allEntries) {
	        if (entry.isHousehold()) {
	            householdEntries.add(entry);
	        } else {
	            entries.add(entry);
	        }
	    }
	}
	
	private void sortEntriesByCompensationFrom(List<SSSContributionTableEntry> entriesToSort) {
		Collections.sort(entriesToSort, (o1, o2) -> o1.getCompensationFrom().compareTo(o2.getCompensationFrom()));
	}

	public boolean isValidEntry(SSSContributionTableEntry other) {
	    List<SSSContributionTableEntry> entriesToCheck = other.isHousehold() ? householdEntries : entries;
	    
		sortEntriesByCompensationFrom(entriesToCheck);
		
		for (SSSContributionTableEntry entry : entriesToCheck) {
			if (entry.overlapsWith(other)) {
				return false;
			}
		}
		return true;
	}

	public BigDecimal getEmployeeContribution(BigDecimal compensation) {
		return getEmployeeContribution(compensation, false);
	}
	
	public BigDecimal getEmployeeContribution(BigDecimal compensation, boolean household) {
	    List<SSSContributionTableEntry> entriesToCheck = household ? householdEntries : entries;
	    
	    Optional<SSSContributionTableEntry> matchingEntry = entriesToCheck.stream()
            .filter(entry -> entry.contains(compensation))
            .findFirst();
	    
	    if (matchingEntry.isPresent()) {
	        return matchingEntry.get().getEmployeeContribution();
	    } else {
	        throw new NoMatchingSssContributionTableEntryException("No SSS contribution table entry defined for compensation " + FormatterUtil.formatAmount(compensation));
	    }
	}
	
    public BigDecimal getEmployerContribution(BigDecimal compensation, boolean household) {
        List<SSSContributionTableEntry> entriesToCheck = household ? householdEntries : entries;
        
        return entriesToCheck.stream()
            .filter(entry -> entry.contains(compensation))
            .findFirst()
            .get().getEmployerContribution();
    }

    public BigDecimal getEmployeeCompensation(BigDecimal compensation, boolean household) {
        List<SSSContributionTableEntry> entriesToCheck = household ? householdEntries : entries;
        
        return entriesToCheck.stream()
                .filter(entry -> entry.contains(compensation))
                .findFirst()
                .get().getEmployeeCompensation();
    }

	public BigDecimal getEmployeeProvidentFundContribution(BigDecimal compensation, boolean household) {
	    List<SSSContributionTableEntry> entriesToCheck = household ? householdEntries : entries;
	    
	    Optional<SSSContributionTableEntry> matchingEntry = entriesToCheck.stream()
            .filter(entry -> entry.contains(compensation))
            .findFirst();
	    
	    if (matchingEntry.isPresent()) {
	        return matchingEntry.get().getEmployeeProvidentFundContribution();
	    } else {
	        throw new NoMatchingSssContributionTableEntryException("No SSS contribution table entry defined for compensation " + FormatterUtil.formatAmount(compensation));
	    }
	}

}
