CREATE DATABASE IF NOT EXISTS employee_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE employee_db;

CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, email) VALUES
('admin', 'admin123', 'admin@gmail.com')
ON DUPLICATE KEY UPDATE password = 'admin123', email = 'admin@gmail.com';

CREATE TABLE IF NOT EXISTS employees (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    department  VARCHAR(50)  NOT NULL,
    salary      DECIMAL(10, 2) NOT NULL CHECK (salary >= 0),
    hire_date   DATE NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO employees (first_name, last_name, email, department, salary, hire_date) VALUES
('Alice', 'Johnson', 'alice.johnson@company.com', 'Engineering', 75000.00, '2022-03-15'),
('Bob', 'Smith', 'bob.smith@company.com', 'HR', 55000.00, '2021-08-01'),
('Carol', 'Davis', 'carol.davis@company.com', 'Finance', 68000.00, '2023-01-10');
