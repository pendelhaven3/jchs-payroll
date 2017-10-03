package com.jchs.payrollapp.model.search;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.PayslipAdjustmentType;

public class PayslipAdjustmentSearchCriteria {

    private Employee employee;
    private PayslipAdjustmentType type;
    private String contributionMonth;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PayslipAdjustmentType getType() {
        return type;
    }

    public void setType(PayslipAdjustmentType type) {
        this.type = type;
    }

    public String getContributionMonth() {
        return contributionMonth;
    }

    public void setContributionMonth(String contributionMonth) {
        this.contributionMonth = contributionMonth;
    }

}
