<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${editMode ? 'Update Employee' : 'Add Employee'}" scope="request"/>
<jsp:include page="header.jsp"/>

<div class="page-header">
    <h1>${editMode ? 'Update Employee' : 'Add Employee Details'}</h1>
    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/employees/">Back to list</a>
</div>

<c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
</c:if>

<div class="card">
    <form method="post" action="${pageContext.request.contextPath}/employees/${editMode ? 'update' : 'add'}">
        <c:if test="${editMode}">
            <input type="hidden" name="id" value="${employee.id}">
        </c:if>
        <div class="form-grid">
            <div class="field">
                <label for="firstName">First Name</label>
                <input type="text" id="firstName" name="firstName" required maxlength="50"
                       value="${employee.firstName}">
            </div>
            <div class="field">
                <label for="lastName">Last Name</label>
                <input type="text" id="lastName" name="lastName" required maxlength="50"
                       value="${employee.lastName}">
            </div>
            <div class="field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required maxlength="100"
                       value="${employee.email}">
            </div>
            <div class="field">
                <label for="department">Department</label>
                <input type="text" id="department" name="department" required maxlength="50"
                       value="${employee.department}" placeholder="e.g. Engineering">
            </div>
            <div class="field">
                <label for="salary">Annual Salary ($)</label>
                <input type="number" id="salary" name="salary" required min="0" step="0.01"
                       value="${employee.salary}">
            </div>
            <div class="field">
                <label for="hireDate">Hire Date</label>
                <input type="date" id="hireDate" name="hireDate" required
                       value="${employee.hireDate}">
            </div>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">${editMode ? 'Update Employee' : 'Add Employee'}</button>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/employees/">Cancel</a>
        </div>
    </form>
</div>

<jsp:include page="footer.jsp"/>
