package com.ems.util;

import com.ems.model.Employee;
import com.ems.model.SalaryBreakdown;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public final class EmployeeHtmlRenderer {

    private EmployeeHtmlRenderer() {
    }

    public static String layout(HttpServletRequest request, String title, String body) {
        String ctx = request.getContextPath();
        String user = request.getSession() != null
                ? String.valueOf(request.getSession().getAttribute("username"))
                : "";

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>%s | Employee Management System</title>
                    <link rel="stylesheet" href="%s/css/app.css">
                </head>
                <body>
                <nav class="navbar">
                    <div class="navbar-brand">EMS</div>
                    <div class="navbar-links">
                        <a class="nav-link" href="%s/dashboard.html">Dashboard</a>
                        <a class="nav-link" href="%s/employees/">Employees</a>
                        <a class="nav-link" href="%s/employees/add">Add Employee</a>
                        <a class="nav-link" href="%s/logout">Sign Out</a>
                    </div>
                    <div class="navbar-user">Signed in as <strong>%s</strong></div>
                </nav>
                <main class="container">%s</main>
                </body>
                </html>
                """.formatted(
                HtmlUtil.escape(title), ctx, ctx, ctx, ctx, ctx,
                HtmlUtil.escape(user), body);
    }

    public static String listPage(HttpServletRequest request, List<Employee> employees,
                                  BigDecimal totalPayroll, String error) {
        String ctx = request.getContextPath();
        StringBuilder alerts = new StringBuilder();
        String success = request.getParameter("success");
        if ("added".equals(success)) {
            alerts.append("<div class=\"alert alert-success\">Employee added successfully.</div>");
        } else if ("updated".equals(success)) {
            alerts.append("<div class=\"alert alert-success\">Employee updated successfully.</div>");
        } else if ("deleted".equals(success)) {
            alerts.append("<div class=\"alert alert-success\">Employee deleted successfully.</div>");
        }
        if ("notfound".equals(request.getParameter("error"))) {
            alerts.append("<div class=\"alert alert-error\">Employee not found.</div>");
        }
        if (error != null && !error.isBlank()) {
            alerts.append("<div class=\"alert alert-error\">").append(HtmlUtil.escape(error)).append("</div>");
        }

        StringBuilder table = new StringBuilder();
        if (employees.isEmpty()) {
            table.append("""
                    <div class="empty-state">
                        <p>No employees yet.</p>
                        <a class="btn btn-primary" style="margin-top:1rem;" href="%s/employees/add">Add first employee</a>
                    </div>
                    """.formatted(ctx));
        } else {
            table.append("<table><thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Department</th>"
                    + "<th>Annual Salary</th><th>Hire Date</th><th>Actions</th></tr></thead><tbody>");
            for (Employee emp : employees) {
                table.append("<tr><td>").append(emp.getId()).append("</td><td>")
                        .append(HtmlUtil.escape(emp.getFirstName())).append(" ")
                        .append(HtmlUtil.escape(emp.getLastName())).append("</td><td>")
                        .append(HtmlUtil.escape(emp.getEmail())).append("</td><td>")
                        .append(HtmlUtil.escape(emp.getDepartment())).append("</td><td>$")
                        .append(HtmlUtil.money(emp.getSalary())).append("</td><td>")
                        .append(emp.getHireDate()).append("</td><td><div class=\"actions\">")
                        .append("<a class=\"btn btn-secondary btn-sm\" href=\"")
                        .append(ctx).append("/employees/view?id=").append(emp.getId())
                        .append("\">View</a> ")
                        .append("<a class=\"btn btn-secondary btn-sm\" href=\"")
                        .append(ctx).append("/employees/edit?id=").append(emp.getId())
                        .append("\">Update</a> ")
                        .append("<a class=\"btn btn-primary btn-sm\" href=\"")
                        .append(ctx).append("/employees/salary?id=").append(emp.getId())
                        .append("\">Salary</a> ")
                        .append("<form class=\"inline-form\" method=\"post\" action=\"")
                        .append(ctx).append("/employees/delete\" onsubmit=\"return confirm('Delete this employee?');\">")
                        .append("<input type=\"hidden\" name=\"id\" value=\"").append(emp.getId())
                        .append("\"><button type=\"submit\" class=\"btn btn-danger btn-sm\">Delete</button></form>")
                        .append("</div></td></tr>");
            }
            table.append("</tbody></table>");
        }

        String body = """
                <div class="page-header">
                    <h1>Employee Details</h1>
                    <a class="btn btn-primary" href="%s/employees/add">+ Add Employee</a>
                </div>
                %s
                <div class="stats">
                    <div class="stat-box"><div class="label">Total Employees</div><div class="value">%d</div></div>
                    <div class="stat-box"><div class="label">Total Annual Payroll</div><div class="value">$%s</div></div>
                </div>
                <div class="card">%s</div>
                """.formatted(ctx, alerts, employees.size(), HtmlUtil.money(totalPayroll), table);

        return layout(request, "Employees", body);
    }

    public static String formPage(HttpServletRequest request, Employee employee,
                                  boolean editMode, String error) {
        String ctx = request.getContextPath();
        String firstName = employee != null ? HtmlUtil.attr(employee.getFirstName()) : "";
        String lastName = employee != null ? HtmlUtil.attr(employee.getLastName()) : "";
        String email = employee != null ? HtmlUtil.attr(employee.getEmail()) : "";
        String department = employee != null ? HtmlUtil.attr(employee.getDepartment()) : "";
        String salary = employee != null && employee.getSalary() != null
                ? HtmlUtil.attr(employee.getSalary().toPlainString()) : "";
        String hireDate = employee != null && employee.getHireDate() != null
                ? employee.getHireDate().toString() : "";
        String idField = editMode && employee != null
                ? "<input type=\"hidden\" name=\"id\" value=\"" + employee.getId() + "\">" : "";
        String alert = error != null && !error.isBlank()
                ? "<div class=\"alert alert-error\">" + HtmlUtil.escape(error) + "</div>" : "";

        String body = """
                <div class="page-header">
                    <h1>%s</h1>
                    <a class="btn btn-secondary" href="%s/employees/">Back to list</a>
                </div>
                %s
                <div class="card">
                    <form method="post" action="%s/employees/%s">
                        %s
                        <div class="form-grid">
                            <div class="field"><label for="firstName">First Name</label>
                                <input type="text" id="firstName" name="firstName" required maxlength="50" value="%s"></div>
                            <div class="field"><label for="lastName">Last Name</label>
                                <input type="text" id="lastName" name="lastName" required maxlength="50" value="%s"></div>
                            <div class="field"><label for="email">Email</label>
                                <input type="email" id="email" name="email" required maxlength="100" value="%s"></div>
                            <div class="field"><label for="department">Department</label>
                                <input type="text" id="department" name="department" required maxlength="50" value="%s"></div>
                            <div class="field"><label for="salary">Annual Salary ($)</label>
                                <input type="number" id="salary" name="salary" required min="0" step="0.01" value="%s"></div>
                            <div class="field"><label for="hireDate">Hire Date</label>
                                <input type="date" id="hireDate" name="hireDate" required value="%s"></div>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">%s</button>
                            <a class="btn btn-secondary" href="%s/employees/">Cancel</a>
                        </div>
                    </form>
                </div>
                """.formatted(
                editMode ? "Update Employee" : "Add Employee Details",
                ctx, alert, ctx, editMode ? "update" : "add", idField,
                firstName, lastName, email, department, salary, hireDate,
                editMode ? "Update Employee" : "Add Employee", ctx);

        return layout(request, editMode ? "Update Employee" : "Add Employee", body);
    }

    public static String viewPage(HttpServletRequest request, Employee employee) {
        String ctx = request.getContextPath();
        String body = """
                <div class="page-header">
                    <h1>Employee Details</h1>
                    <div class="actions">
                        <a class="btn btn-secondary" href="%s/employees/">Back</a>
                        <a class="btn btn-primary" href="%s/employees/edit?id=%d">Update</a>
                        <a class="btn btn-primary" href="%s/employees/salary?id=%d">Calculate Salary</a>
                    </div>
                </div>
                <div class="card"><div class="detail-grid">
                    <div class="detail-item"><div class="label">Employee ID</div><div class="value">%d</div></div>
                    <div class="detail-item"><div class="label">Full Name</div><div class="value">%s %s</div></div>
                    <div class="detail-item"><div class="label">Email</div><div class="value">%s</div></div>
                    <div class="detail-item"><div class="label">Department</div><div class="value">%s</div></div>
                    <div class="detail-item"><div class="label">Annual Salary</div><div class="value">$%s</div></div>
                    <div class="detail-item"><div class="label">Hire Date</div><div class="value">%s</div></div>
                </div></div>
                """.formatted(
                ctx, ctx, employee.getId(), ctx, employee.getId(),
                employee.getId(),
                HtmlUtil.escape(employee.getFirstName()), HtmlUtil.escape(employee.getLastName()),
                HtmlUtil.escape(employee.getEmail()), HtmlUtil.escape(employee.getDepartment()),
                HtmlUtil.money(employee.getSalary()), employee.getHireDate());

        return layout(request, "View Employee", body);
    }

    public static String salaryPage(HttpServletRequest request, Employee employee, SalaryBreakdown salary) {
        String ctx = request.getContextPath();
        String body = """
                <div class="page-header">
                    <h1>Salary Calculation</h1>
                    <a class="btn btn-secondary" href="%s/employees/view?id=%d">Back to details</a>
                </div>
                <div class="card">
                    <p style="margin-bottom:1.25rem;color:#64748b;">Salary breakdown for <strong>%s</strong> (ID: %d)</p>
                    <h2 style="font-size:1rem;margin-bottom:1rem;color:#475569;">Gross Pay</h2>
                    <div class="salary-grid" style="margin-bottom:1.5rem;">
                        <div class="salary-box"><div class="label">Annual Gross</div><div class="amount">$%s</div></div>
                        <div class="salary-box"><div class="label">Monthly Gross</div><div class="amount">$%s</div></div>
                    </div>
                    <h2 style="font-size:1rem;margin-bottom:1rem;color:#475569;">Deductions (10%% Tax + 12%% PF)</h2>
                    <div class="salary-grid" style="margin-bottom:1.5rem;">
                        <div class="salary-box"><div class="label">Income Tax (10%%)</div><div class="amount" style="color:#b91c1c;">-$%s</div></div>
                        <div class="salary-box"><div class="label">Provident Fund (12%%)</div><div class="amount" style="color:#b91c1c;">-$%s</div></div>
                        <div class="salary-box"><div class="label">Total Deductions</div><div class="amount" style="color:#b91c1c;">-$%s</div></div>
                    </div>
                    <h2 style="font-size:1rem;margin-bottom:1rem;color:#475569;">Net Pay</h2>
                    <div class="salary-grid">
                        <div class="salary-box highlight"><div class="label">Net Annual Salary</div><div class="amount">$%s</div></div>
                        <div class="salary-box highlight"><div class="label">Net Monthly Salary</div><div class="amount">$%s</div></div>
                    </div>
                </div>
                """.formatted(
                ctx, employee.getId(),
                HtmlUtil.escape(salary.getEmployeeName()), salary.getEmployeeId(),
                HtmlUtil.money(salary.getAnnualGross()), HtmlUtil.money(salary.getMonthlyGross()),
                HtmlUtil.money(salary.getTaxDeduction()), HtmlUtil.money(salary.getPfDeduction()),
                HtmlUtil.money(salary.getTotalDeductions()),
                HtmlUtil.money(salary.getNetAnnual()), HtmlUtil.money(salary.getNetMonthly()));

        return layout(request, "Salary Calculation", body);
    }

    public static void write(HttpServletResponse response, String html) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(html);
    }
}
