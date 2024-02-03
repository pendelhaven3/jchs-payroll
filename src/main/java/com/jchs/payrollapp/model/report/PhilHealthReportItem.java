package com.jchs.payrollapp.model.report;

import java.math.BigDecimal;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import lombok.Getter;
import lombok.Setter;

@SqlResultSetMapping(name = "philHealthReportItemMapping", 
    classes = {
        @ConstructorResult(targetClass = PhilHealthReportItem.class, columns = {
            @ColumnResult(name = "employeeId", type = Long.class),
            @ColumnResult(name = "employeeName"),
            @ColumnResult(name = "philHealthNumber"),
            @ColumnResult(name = "monthlyPay", type = BigDecimal.class),
            @ColumnResult(name = "due", type = BigDecimal.class)
        })
    }
)

@Entity // DUMMY
@Getter
@Setter
public class PhilHealthReportItem {

    @Id
    private Long id;
    
    private Long employeeId;
    private String employeeName;
    private String philHealthNumber;
    private BigDecimal monthlyPay;
    private BigDecimal due;
 
    public PhilHealthReportItem() { }

    public PhilHealthReportItem(
    		Long employeeId,
    		String employeeName,
    		String philHealthNumber,
    		BigDecimal monthlyPay,
    		BigDecimal due) {
    	this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.philHealthNumber = philHealthNumber;
        this.monthlyPay = monthlyPay;
        this.due = due;
    }

}
