<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="View Employee" scope="request"/>
<jsp:include page="header.jsp"/>

<div class="page-header">
    <h1>Employee Details</h1>
    <div class="actions">
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/employees/">Back</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/employees/edit?id=${employee.id}">Update</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/employees/salary?id=${employee.id}">Calculate Salary</a>
    </div>
</div>

<div class="card">
    <div class="detail-grid">
        <div class="detail-item">
            <div class="label">Employee ID</div>
            <div class="value">${employee.id}</div>
        </div>
        <div class="detail-item">
            <div class="label">Full Name</div>
            <div class="value">${employee.firstName} ${employee.lastName}</div>
        </div>
        <div class="detail-item">
            <div class="label">Email</div>
            <div class="value">${employee.email}</div>
        </div>
        <div class="detail-item">
            <div class="label">Department</div>
            <div class="value">${employee.department}</div>
        </div>
        <div class="detail-item">
            <div class="label">Annual Salary</div>
            <div class="value">$<fmt:formatNumber value="${employee.salary}" minFractionDigits="2" maxFractionDigits="2"/></div>
        </div>
        <div class="detail-item">
            <div class="label">Hire Date</div>
            <div class="value">${employee.hireDate}</div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
