<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Employees" scope="request"/>
<jsp:include page="header.jsp"/>

<div class="page-header">
    <h1>Employee Details</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/employees/add">+ Add Employee</a>
</div>

<c:if test="${param.success == 'added'}">
    <div class="alert alert-success">Employee added successfully.</div>
</c:if>
<c:if test="${param.success == 'updated'}">
    <div class="alert alert-success">Employee updated successfully.</div>
</c:if>
<c:if test="${param.success == 'deleted'}">
    <div class="alert alert-success">Employee deleted successfully.</div>
</c:if>
<c:if test="${param.error == 'notfound'}">
    <div class="alert alert-error">Employee not found.</div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
</c:if>

<div class="stats">
    <div class="stat-box">
        <div class="label">Total Employees</div>
        <div class="value">${employeeCount}</div>
    </div>
    <div class="stat-box">
        <div class="label">Total Annual Payroll</div>
        <div class="value">$<fmt:formatNumber value="${totalPayroll}" minFractionDigits="2" maxFractionDigits="2"/></div>
    </div>
</div>

<div class="card">
    <c:choose>
        <c:when test="${empty employees}">
            <div class="empty-state">
                <p>No employees yet.</p>
                <a class="btn btn-primary" style="margin-top:1rem;" href="${pageContext.request.contextPath}/employees/add">Add first employee</a>
            </div>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Department</th>
                    <th>Annual Salary</th>
                    <th>Hire Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="emp" items="${employees}">
                    <tr>
                        <td>${emp.id}</td>
                        <td>${emp.firstName} ${emp.lastName}</td>
                        <td>${emp.email}</td>
                        <td>${emp.department}</td>
                        <td>$<fmt:formatNumber value="${emp.salary}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td>${emp.hireDate}</td>
                        <td>
                            <div class="actions">
                                <a class="btn btn-secondary btn-sm" href="${pageContext.request.contextPath}/employees/view?id=${emp.id}">View</a>
                                <a class="btn btn-secondary btn-sm" href="${pageContext.request.contextPath}/employees/edit?id=${emp.id}">Update</a>
                                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/employees/salary?id=${emp.id}">Salary</a>
                                <form class="inline-form" method="post" action="${pageContext.request.contextPath}/employees/delete"
                                      onsubmit="return confirm('Delete this employee?');">
                                    <input type="hidden" name="id" value="${emp.id}">
                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="footer.jsp"/>
