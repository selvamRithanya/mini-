package com.ems.servlet;

import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;
import com.ems.model.SalaryBreakdown;
import com.ems.service.SalaryCalculator;
import com.ems.util.EmployeeHtmlRenderer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "EmployeeServlet", urlPatterns = "/employees/*")
public class EmployeeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EmployeeServlet.class.getName());
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        try {
            switch (path) {
                case "/", "/list" -> listEmployees(request, response, null);
                case "/add" -> showForm(request, response, null, false, null);
                case "/view" -> viewEmployee(request, response);
                case "/edit" -> showEditForm(request, response);
                case "/salary" -> showSalary(request, response);
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            try {
                listEmployees(request, response, "Database error. Please try again.");
            } catch (SQLException ex) {
                throw new ServletException(ex);
            }
        } catch (IllegalArgumentException e) {
            // Friendly handling for missing/invalid id values in view/edit/salary
            LOGGER.log(Level.WARNING, "Invalid request parameter", e);
            try {
                listEmployees(request, response, "Invalid or missing employee id.");
            } catch (SQLException ex) {
                throw new ServletException(ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        try {
            switch (path) {
                case "/add" -> createEmployee(request, response);
                case "/update" -> updateEmployee(request, response);
                case "/delete" -> deleteEmployee(request, response);
                default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            try {
                listEmployees(request, response, "Database error. Please try again.");
            } catch (SQLException ex) {
                throw new ServletException(ex);
            }
        } catch (IllegalArgumentException e) {
            boolean editMode = "/update".equals(path);
            showForm(request, response, buildEmployeeFromRequest(request), editMode, e.getMessage());
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response, String error)
            throws SQLException, IOException {

        List<Employee> employees = employeeDAO.getAllEmployees();
        BigDecimal totalPayroll = employees.stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String html = EmployeeHtmlRenderer.listPage(request, employees, totalPayroll, error);
        EmployeeHtmlRenderer.write(response, html);
    }

    private void viewEmployee(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseId(request);
        Optional<Employee> employee = employeeDAO.getEmployeeById(id);
        if (employee.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/employees/?error=notfound");
            return;
        }
        EmployeeHtmlRenderer.write(response, EmployeeHtmlRenderer.viewPage(request, employee.get()));
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseId(request);
        Optional<Employee> employee = employeeDAO.getEmployeeById(id);
        if (employee.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/employees/?error=notfound");
            return;
        }
        showForm(request, response, employee.get(), true, null);
    }

    private void showSalary(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseId(request);
        Optional<Employee> employee = employeeDAO.getEmployeeById(id);
        if (employee.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/employees/?error=notfound");
            return;
        }
        SalaryBreakdown breakdown = SalaryCalculator.calculate(employee.get());
        EmployeeHtmlRenderer.write(response, EmployeeHtmlRenderer.salaryPage(request, employee.get(), breakdown));
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response,
                          Employee employee, boolean editMode, String error) throws IOException {
        EmployeeHtmlRenderer.write(response, EmployeeHtmlRenderer.formPage(request, employee, editMode, error));
    }

    private void createEmployee(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Employee employee = buildEmployeeFromRequest(request);
        validateEmployee(employee, false);
        try {
            employeeDAO.addEmployee(employee);
            response.sendRedirect(request.getContextPath() + "/employees/?success=added");
        } catch (SQLException e) {
            if (isDuplicateKeyError(e)) {
                showForm(request, response, employee, false, "An employee with that email already exists.");
                return;
            }
            throw e;
        }
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Employee employee = buildEmployeeFromRequest(request);
        employee.setId(parseId(request));
        validateEmployee(employee, true);
        try {
            if (!employeeDAO.updateEmployee(employee)) {
                response.sendRedirect(request.getContextPath() + "/employees/?error=notfound");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/employees/?success=updated");
        } catch (SQLException e) {
            if (isDuplicateKeyError(e)) {
                showForm(request, response, employee, true, "An employee with that email already exists.");
                return;
            }
            throw e;
        }
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseId(request);
        if (employeeDAO.deleteEmployee(id)) {
            response.sendRedirect(request.getContextPath() + "/employees/?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/employees/?error=notfound");
        }
    }

    private Employee buildEmployeeFromRequest(HttpServletRequest request) {
        Employee employee = new Employee();
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            try {
                employee.setId(Integer.parseInt(idParam.trim()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid employee id.");
            }
        }
        employee.setFirstName(trim(request.getParameter("firstName")));
        employee.setLastName(trim(request.getParameter("lastName")));
        employee.setEmail(trim(request.getParameter("email")));
        employee.setDepartment(trim(request.getParameter("department")));
        String salaryStr = trim(request.getParameter("salary"));
        if (salaryStr != null && !salaryStr.isEmpty()) {
            try {
                employee.setSalary(new BigDecimal(salaryStr.trim()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Salary must be a valid number.");
            }
        }
        String hireDateStr = trim(request.getParameter("hireDate"));
        if (hireDateStr != null && !hireDateStr.isEmpty()) {
            try {
                employee.setHireDate(LocalDate.parse(hireDateStr));
            } catch (Exception e) {
                throw new IllegalArgumentException("Hire date must be a valid date (YYYY-MM-DD).");
            }
        }
        return employee;
    }

    private void validateEmployee(Employee employee, boolean updating) {
        if (isBlank(employee.getFirstName())) {
            throw new IllegalArgumentException("First name is required.");
        }
        if (isBlank(employee.getLastName())) {
            throw new IllegalArgumentException("Last name is required.");
        }
        if (isBlank(employee.getEmail())) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (isBlank(employee.getDepartment())) {
            throw new IllegalArgumentException("Department is required.");
        }
        if (employee.getSalary() == null || employee.getSalary().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valid annual salary is required.");
        }
        if (employee.getHireDate() == null) {
            throw new IllegalArgumentException("Hire date is required.");
        }
        if (updating && employee.getId() <= 0) {
            throw new IllegalArgumentException("Invalid employee ID.");
        }
    }

    private int parseId(HttpServletRequest request) {
        // Try query parameter first
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            try {
                return Integer.parseInt(idParam.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid employee ID.");
            }
        }

        // Fallback: try to parse numeric id from the path info (e.g. /view/123 or /123)
        String path = request.getPathInfo();
        if (path != null && !path.isBlank()) {
            String[] parts = path.split("/");
            for (int i = parts.length - 1; i >= 0; i--) {
                String part = parts[i];
                if (part == null || part.isBlank()) continue;
                try {
                    return Integer.parseInt(part);
                } catch (NumberFormatException ignored) {
                }
            }
        }

        throw new IllegalArgumentException("Employee ID is required.");
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isDuplicateKeyError(SQLException e) {
        String state = e.getSQLState();
        if (state != null && state.startsWith("23")) return true;
        String msg = e.getMessage();
        if (msg != null) {
            String m = msg.toLowerCase();
            if (m.contains("duplicate") || m.contains("unique") || m.contains("constraint")) return true;
        }
        return false;
    }
}
