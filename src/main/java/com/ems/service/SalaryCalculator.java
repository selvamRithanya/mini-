package com.ems.service;

import com.ems.model.Employee;
import com.ems.model.SalaryBreakdown;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SalaryCalculator {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.10");
    private static final BigDecimal PF_RATE = new BigDecimal("0.12");

    private SalaryCalculator() {
    }

    public static SalaryBreakdown calculate(Employee employee) {
        BigDecimal annualGross = employee.getSalary().setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthlyGross = annualGross.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        BigDecimal taxDeduction = annualGross.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal pfDeduction = annualGross.multiply(PF_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalDeductions = taxDeduction.add(pfDeduction);

        BigDecimal netAnnual = annualGross.subtract(totalDeductions).setScale(2, RoundingMode.HALF_UP);
        BigDecimal netMonthly = netAnnual.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        return new SalaryBreakdown(
                employee.getId(),
                employee.getFullName(),
                annualGross,
                monthlyGross,
                taxDeduction,
                pfDeduction,
                totalDeductions,
                netMonthly,
                netAnnual);
    }
}
