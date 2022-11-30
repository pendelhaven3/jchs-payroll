package com.jchs.payrollapp.model.report;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhilHealthReport {

	private YearMonth yearMonth;
    private List<PhilHealthReportItem> items = new ArrayList<>();

    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public BigDecimal getTotalMonthlyPay() {
        return items.stream()
                .map(item -> item.getMonthlyPay())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
    public BigDecimal getTotalDue() {
        return items.stream()
                .map(item -> item.getDue())
                .reduce(BigDecimal.ZERO, (x,y) -> x.add(y));
    }
    
}
