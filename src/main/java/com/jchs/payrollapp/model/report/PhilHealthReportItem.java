package com.jchs.payrollapp.model.report;

import java.math.BigDecimal;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "philHealthReportItemMapping", 
    classes = {
        @ConstructorResult(targetClass = PhilHealthReportItem.class, columns = {
            @ColumnResult(name = "employeeName"),
            @ColumnResult(name = "philHealthNumber"),
            @ColumnResult(name = "monthlyPay", type = BigDecimal.class),
            @ColumnResult(name = "due", type = BigDecimal.class)
        })
    }
)

@Entity // DUMMY
public class PhilHealthReportItem {

    @Id
    private Long id;
    
    private String employeeName;
    private String philHealthNumber;
    private BigDecimal monthlyPay;
    private BigDecimal due;
 
    public PhilHealthReportItem() { }

    public PhilHealthReportItem(String employeeName, String philHealthNumber, BigDecimal monthlyPay, BigDecimal due) {
        this.employeeName = employeeName;
        this.philHealthNumber = philHealthNumber;
        this.monthlyPay = monthlyPay;
        this.due = due;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPhilHealthNumber() {
        return philHealthNumber;
    }

    public void setPhilHealthNumber(String philHealthNumber) {
        this.philHealthNumber = philHealthNumber;
    }

    public BigDecimal getMonthlyPay() {
        return monthlyPay;
    }

    public void setMonthlyPay(BigDecimal monthlyPay) {
        this.monthlyPay = monthlyPay;
    }

    public BigDecimal getDue() {
        return due;
    }

    public void setDue(BigDecimal due) {
        this.due = due;
    }
    
}
