package com.ems.model;

import java.math.BigDecimal;

public class SalaryBreakdown {

    private final int employeeId;
    private final String employeeName;
    private final BigDecimal annualGross;
    private final BigDecimal monthlyGross;
    private final BigDecimal taxDeduction;
    private final BigDecimal pfDeduction;
    private final BigDecimal totalDeductions;
    private final BigDecimal netMonthly;
    private final BigDecimal netAnnual;

    public SalaryBreakdown(int employeeId, String employeeName, BigDecimal annualGross,
                           BigDecimal monthlyGross, BigDecimal taxDeduction, BigDecimal pfDeduction,
                           BigDecimal totalDeductions, BigDecimal netMonthly, BigDecimal netAnnual) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.annualGross = annualGross;
        this.monthlyGross = monthlyGross;
        this.taxDeduction = taxDeduction;
        this.pfDeduction = pfDeduction;
        this.totalDeductions = totalDeductions;
        this.netMonthly = netMonthly;
        this.netAnnual = netAnnual;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public BigDecimal getAnnualGross() {
        return annualGross;
    }

    public BigDecimal getMonthlyGross() {
        return monthlyGross;
    }

    public BigDecimal getTaxDeduction() {
        return taxDeduction;
    }

    public BigDecimal getPfDeduction() {
        return pfDeduction;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public BigDecimal getNetMonthly() {
        return netMonthly;
    }

    public BigDecimal getNetAnnual() {
        return netAnnual;
    }
}
