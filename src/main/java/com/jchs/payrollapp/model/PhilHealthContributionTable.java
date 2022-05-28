package com.jchs.payrollapp.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class PhilHealthContributionTable {

    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    
    @Id
    private Long id;
    
    @Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal floor;
    
    @Column(precision = 9, scale = 2, nullable = false)
	private BigDecimal ceiling;
    
    @Column(precision = 9, scale = 2, nullable = false)
    private BigDecimal multiplier;
    
    @Column(precision = 9, scale = 0, nullable = false)
    private BigDecimal householdMonthlyContribution;
    
    @Transient
    private BigDecimal floorPremium;
    
    @Transient
    private BigDecimal ceilingPremium;
    
    @Transient
    private BigDecimal actualMultiplier;

    public BigDecimal getEmployeeShare(BigDecimal salary) {
    	return getEmployeeShare(salary, false);
    }
    
    public BigDecimal getEmployeeShare(BigDecimal salary, boolean household) {
        if (household) {
            return householdMonthlyContribution.divide(BigDecimal.valueOf(2L));
        }
        
        if (salary.compareTo(floor) <= 0) {
            return getFloorPremium();
        } else if (salary.compareTo(ceiling) >= 0) {
            return getCeilingPremium();
        } else {
            return salary.multiply(multiplier.divide(ONE_HUNDRED)).divide(TWO, 2, RoundingMode.CEILING);
        }
    }
    
    private BigDecimal getFloorPremium() {
        if (floorPremium == null) {
            floorPremium = floor.multiply(multiplier.divide(ONE_HUNDRED)).divide(TWO, RoundingMode.CEILING);
        }
        return floorPremium;
    }
	
    private BigDecimal getCeilingPremium() {
        if (ceilingPremium == null) {
            ceilingPremium = ceiling.multiply(multiplier.divide(ONE_HUNDRED)).divide(TWO, RoundingMode.CEILING);
        }
        return ceilingPremium;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PhilHealthContributionTable other = (PhilHealthContributionTable) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public BigDecimal getFloor() {
        return floor;
    }

    public void setFloor(BigDecimal floor) {
        this.floor = floor;
    }

    public BigDecimal getCeiling() {
        return ceiling;
    }

    public void setCeiling(BigDecimal ceiling) {
        this.ceiling = ceiling;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public BigDecimal getHouseholdMonthlyContribution() {
        return householdMonthlyContribution;
    }

    public void setHouseholdMonthlyContribution(BigDecimal householdMonthlyContribution) {
        this.householdMonthlyContribution = householdMonthlyContribution;
    }
    
}
