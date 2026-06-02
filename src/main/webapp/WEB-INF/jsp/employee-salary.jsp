<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Salary Calculation" scope="request"/>
<jsp:include page="header.jsp"/>

<div class="page-header">
    <h1>Salary Calculation</h1>
    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/employees/view?id=${employee.id}">Back to details</a>
</div>

<div class="card">
    <p style="margin-bottom:1.25rem;color:#64748b;">
        Salary breakdown for <strong>${salary.employeeName}</strong> (ID: ${salary.employeeId})
    </p>

    <h2 style="font-size:1rem;margin-bottom:1rem;color:#475569;">Gross Pay</h2>
    <div class="salary-grid" style="margin-bottom:1.5rem;">
        <div class="salary-box">
            <div class="label">Annual Gross</div>
            <div class="amount">$<fmt:formatNumber value="${salary.annualGross}" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>
        <div class="salary-box">
            <div class="label">Monthly Gross</div>
            <div class="amount">$<fmt:formatNumber value="${salary.monthlyGross}" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>
    </div>

    <h2 style="font-size:1rem;margin-bottom:1rem;color:#475569;">Deductions (10% Tax + 12% PF)</h2>
    <div class="salary-grid" style="margin-bottom:1.5rem;">
        <div class="salary-box">
            <div class="label">Income Tax (10%)</div>
            <div class="amount" style="color:#b91c1c;">-$<fmt:formatNumber value="${salary.taxDeduction}" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>
        <div class="salary-box">
            <div class="label">Provident Fund (12%)</div>
            <div class="amount" style="color:#b91c1c;">-$<fmt:formatNumber value="${salary.pfDeduction}" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>
        <div class="salary-box">
            <div class="label">Total Deductions</div>
            <div class="amount" style="color:#b91c1c;">-$<fmt:formatNumber value="${salary.totalDeductions}" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>
    </div>

    <h2 style="font-size:1rem;margin-bottom:1rem;color:#475569;">Net Pay</h2>
    <div class="salary-grid">
        <div class="salary-box highlight">
            <div class="label">Net Annual Salary</div>
            <div class="amount">$<fmt:formatNumber value="${salary.netAnnual}" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>
        <div class="salary-box highlight">
            <div class="label">Net Monthly Salary</div>
            <div class="amount">$<fmt:formatNumber value="${salary.netMonthly}" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
