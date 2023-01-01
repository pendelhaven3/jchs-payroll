package com.jchs.payrollapp.model.report;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagIbigReport {

	private YearMonth yearMonth;
    private List<PagIbigReportItem> items = new ArrayList<>();

    public boolean isEmpty() {
        return items.isEmpty();
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
    
}
