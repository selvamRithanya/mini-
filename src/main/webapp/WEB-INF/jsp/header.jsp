<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} | Employee Management System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/app.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-brand">EMS</div>
    <div class="navbar-links">
        <a class="nav-link" href="${pageContext.request.contextPath}/dashboard.html">Dashboard</a>
        <a class="nav-link" href="${pageContext.request.contextPath}/employees/">Employees</a>
        <a class="nav-link" href="${pageContext.request.contextPath}/employees/add">Add Employee</a>
        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Sign Out</a>
    </div>
    <div class="navbar-user">Signed in as <strong>${sessionScope.username}</strong></div>
</nav>
<main class="container">
