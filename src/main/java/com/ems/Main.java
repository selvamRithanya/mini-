package com.ems;

import com.ems.dao.EmployeeDAO;
import com.ems.model.Employee;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final EmployeeDAO dao = new EmployeeDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Employee Management System ===");
        System.out.println("Java + JDBC + MySQL\n");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> listEmployees();
                    case "2" -> addEmployee();
                    case "3" -> updateEmployee();
                    case "4" -> deleteEmployee();
                    case "5" -> searchById();
                    case "0" -> {
                        running = false;
                        System.out.println("Goodbye.");
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid input: " + e.getMessage());
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("1. List all employees");
        System.out.println("2. Add employee");
        System.out.println("3. Update employee");
        System.out.println("4. Delete employee");
        System.out.println("5. Search by ID");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static void listEmployees() throws SQLException {
        List<Employee> employees = dao.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        System.out.println("\n--- Employees ---");
        for (Employee e : employees) {
            System.out.println(e);
        }
        System.out.println("Total: " + employees.size());
    }

    private static void addEmployee() throws SQLException {
        Employee employee = readEmployeeFromInput(false);
        int id = dao.addEmployee(employee);
        System.out.println("Employee added with ID: " + id);
    }

    private static void updateEmployee() throws SQLException {
        int id = readInt("Enter employee ID to update: ");
        Optional<Employee> existing = dao.getEmployeeById(id);
        if (existing.isEmpty()) {
            System.out.println("No employee found with ID " + id);
            return;
        }

        System.out.println("Current: " + existing.get());
        System.out.println("Enter new details (press Enter to keep current value):");

        Employee updated = readEmployeeFromInput(true);
        updated.setId(id);

        Employee current = existing.get();
        if (updated.getFirstName() == null) updated.setFirstName(current.getFirstName());
        if (updated.getLastName() == null) updated.setLastName(current.getLastName());
        if (updated.getEmail() == null) updated.setEmail(current.getEmail());
        if (updated.getDepartment() == null) updated.setDepartment(current.getDepartment());
        if (updated.getSalary() == null) updated.setSalary(current.getSalary());
        if (updated.getHireDate() == null) updated.setHireDate(current.getHireDate());

        if (dao.updateEmployee(updated)) {
            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Update failed.");
        }
    }

    private static void deleteEmployee() throws SQLException {
        int id = readInt("Enter employee ID to delete: ");
        System.out.print("Are you sure? (y/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Delete cancelled.");
            return;
        }
        if (dao.deleteEmployee(id)) {
            System.out.println("Employee deleted.");
        } else {
            System.out.println("No employee found with ID " + id);
        }
    }

    private static void searchById() throws SQLException {
        int id = readInt("Enter employee ID: ");
        Optional<Employee> employee = dao.getEmployeeById(id);
        employee.ifPresentOrElse(
                e -> System.out.println(e),
                () -> System.out.println("No employee found with ID " + id));
    }

    private static Employee readEmployeeFromInput(boolean partial) {
        Employee employee = new Employee();

        employee.setFirstName(readOptionalString("First name", partial));
        employee.setLastName(readOptionalString("Last name", partial));
        employee.setEmail(readOptionalString("Email", partial));
        employee.setDepartment(readOptionalString("Department", partial));
        employee.setSalary(readOptionalSalary("Salary", partial));
        employee.setHireDate(readOptionalDate("Hire date (yyyy-MM-dd)", partial));

        if (!partial) {
            require(employee.getFirstName(), "First name");
            require(employee.getLastName(), "Last name");
            require(employee.getEmail(), "Email");
            require(employee.getDepartment(), "Department");
            if (employee.getSalary() == null) {
                throw new IllegalArgumentException("Salary is required.");
            }
            if (employee.getHireDate() == null) {
                throw new IllegalArgumentException("Hire date is required.");
            }
        }
        return employee;
    }

    private static String readOptionalString(String label, boolean partial) {
        System.out.print(label + ": ");
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            return partial ? null : "";
        }
        return value;
    }

    private static BigDecimal readOptionalSalary(String label, boolean partial) {
        System.out.print(label + ": ");
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            return partial ? null : null;
        }
        try {
            BigDecimal salary = new BigDecimal(value);
            if (salary.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Salary cannot be negative.");
            }
            return salary;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid salary format.");
        }
    }

    private static LocalDate readOptionalDate(String label, boolean partial) {
        System.out.print(label + ": ");
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            return partial ? null : null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date. Use yyyy-MM-dd.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void require(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required.");
        }
    }
}
