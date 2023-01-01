package com.jchs.payrollapp.model.report;

import java.math.BigDecimal;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import lombok.Getter;
import lombok.Setter;

@SqlResultSetMapping(name = "sssReportItemMapping", 
    classes = {
        @ConstructorResult(targetClass = SSSReportItem.class, columns = {
            @ColumnResult(name = "employeeName"),
            @ColumnResult(name = "sssNumber"),
            @ColumnResult(name = "monthlyPay", type = BigDecimal.class),
            @ColumnResult(name = "employeeContribution", type = BigDecimal.class),
            @ColumnResult(name = "employerContribution", type = BigDecimal.class),
            @ColumnResult(name = "employeeCompensation", type = BigDecimal.class),
            @ColumnResult(name = "employeeProvidentFundContribution", type = BigDecimal.class),
            @ColumnResult(name = "employerProvidentFundContribution", type = BigDecimal.class)
        })
    }
)

@Entity // DUMMY
@Getter
@Setter
public class SSSReportItem {

    @Id
    private Long id;
    
    private String employeeName;
    private String sssNumber;
    private BigDecimal monthlyPay;
    private BigDecimal employeeContribution;
    private BigDecimal employerContribution;
    private BigDecimal employeeCompensation;
    private BigDecimal employeeProvidentFundContribution;
    private BigDecimal employerProvidentFundContribution;
 
    public SSSReportItem() { }

    public SSSReportItem(String employeeName, String sssNumber, BigDecimal monthlyPay, BigDecimal employeeContribution,
            BigDecimal employerContribution, BigDecimal employeeCompensation,
            BigDecimal employeeProvidentFundContribution, BigDecimal employerProvidentFundContribution
    ) {
        this.employeeName = employeeName;
        this.sssNumber = sssNumber;
        this.monthlyPay = monthlyPay;
        this.employeeContribution = employeeContribution;
        this.employerContribution = employerContribution;
        this.employeeCompensation = employeeCompensation;
        this.employeeProvidentFundContribution = employeeProvidentFundContribution;
        this.employerProvidentFundContribution = employerProvidentFundContribution;
    }

    public BigDecimal getTotalContribution() {
        return employeeContribution.add(employerContribution);
    }
    
    public BigDecimal getTotalProvidentFundContribution() {
        return employeeProvidentFundContribution.add(employerProvidentFundContribution);
    }
    
}
