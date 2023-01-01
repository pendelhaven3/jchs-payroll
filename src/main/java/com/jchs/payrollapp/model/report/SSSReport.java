package com.jchs.payrollapp.model.report;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SSSReport {

	private YearMonth yearMonth;
    private List<SSSReportItem> items = new ArrayList<>();

    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public BigDecimal getTotalMonthlyPay() {
        return items.stream()
                .map(item -> item.getMonthlyPay())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalEmployeeContribution() {
        return items.stream()
                .map(item -> item.getEmployeeContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalEmployerContribution() {
        return items.stream()
                .map(item -> item.getEmployerContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalContribution() {
        return items.stream()
                .map(item -> item.getTotalContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalEmployeeCompensation() {
        return items.stream()
                .map(item -> item.getEmployeeCompensation())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalEmployeeProvidentFundContribution() {
        return items.stream()
                .map(item -> item.getEmployeeProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalEmployerProvidentFundContribution() {
        return items.stream()
                .map(item -> item.getEmployerProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalProvidentFundContribution() {
        return items.stream()
                .map(item -> item.getTotalProvidentFundContribution())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
}
