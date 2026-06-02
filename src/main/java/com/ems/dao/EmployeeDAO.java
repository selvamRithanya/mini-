package com.ems.dao;

import com.ems.db.DatabaseConnection;
import com.ems.model.Employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO {

    private static final String INSERT =
            "INSERT INTO employees (first_name, last_name, email, department, salary, hire_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL =
            "SELECT id, first_name, last_name, email, department, salary, hire_date "
                    + "FROM employees ORDER BY id";

    private static final String SELECT_BY_ID =
            "SELECT id, first_name, last_name, email, department, salary, hire_date "
                    + "FROM employees WHERE id = ?";

    private static final String UPDATE =
            "UPDATE employees SET first_name = ?, last_name = ?, email = ?, "
                    + "department = ?, salary = ?, hire_date = ? WHERE id = ?";

    private static final String DELETE =
            "DELETE FROM employees WHERE id = ?";

    public int addEmployee(Employee employee) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getDepartment());
            ps.setBigDecimal(5, employee.getSalary());
            ps.setDate(6, Date.valueOf(employee.getHireDate()));

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insert failed, no rows affected.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Insert failed, no ID obtained.");
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employees.add(mapRow(rs));
            }
        }
        return employees;
    }

    public Optional<Employee> getEmployeeById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getEmail());
            ps.setString(4, employee.getDepartment());
            ps.setBigDecimal(5, employee.getSalary());
            ps.setDate(6, Date.valueOf(employee.getHireDate()));
            ps.setInt(7, employee.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteEmployee(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("department"),
                rs.getBigDecimal("salary"),
                rs.getDate("hire_date").toLocalDate());
    }
}
