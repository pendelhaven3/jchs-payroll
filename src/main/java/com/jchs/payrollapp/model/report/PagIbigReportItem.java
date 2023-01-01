package com.jchs.payrollapp.model.report;

import java.math.BigDecimal;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import lombok.Getter;
import lombok.Setter;

@SqlResultSetMapping(name = "pagIbigReportItemMapping", 
    classes = {
        @ConstructorResult(targetClass = PagIbigReportItem.class, columns = {
            @ColumnResult(name = "employeeName"),
            @ColumnResult(name = "pagIbigNumber"),
            @ColumnResult(name = "employeeContribution", type = BigDecimal.class),
            @ColumnResult(name = "employerContribution", type = BigDecimal.class)
        })
    }
)

@Entity // DUMMY
@Getter
@Setter
public class PagIbigReportItem {

    @Id
    private Long id;
    
    private String employeeName;
    private String pagIbigNumber;
    private BigDecimal employeeContribution;
    private BigDecimal employerContribution;
 
    public PagIbigReportItem() { }

    public PagIbigReportItem(String employeeName, String pagIbigNumber, BigDecimal employeeContribution, BigDecimal employerContribution) {
        this.employeeName = employeeName;
        this.pagIbigNumber = pagIbigNumber;
        this.employeeContribution = employeeContribution;
        this.employerContribution = employerContribution;
    }

    public BigDecimal getTotalContribution() {
        return employeeContribution.add(employerContribution);
    }
    
}
