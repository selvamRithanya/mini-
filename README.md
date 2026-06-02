# Employee Management System

Web login and console employee management built with **Java**, **JDBC**, **MySQL**, and **HTML**.

## Features

- **Login page** — username, password, and mail ID sign-in
- **Add employee** — first name, last name, email, department, salary, hire date
- **View employee** — full details for one employee
- **Update employee** — edit existing records
- **Delete employee** — remove with confirmation
- **Calculate salary** — annual/monthly gross, tax (10%), PF (12%), net pay
- Total payroll summary on employee list
- JDBC + prepared statements

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8+

## Setup

### 1. Create the database

```bash
mysql -u root -p < sql/schema.sql
```

### 2. Configure connection

Edit `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/employee_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=your_mysql_password
```

### 3. Run the web app

```bash
mvn jetty:run
```

Open in browser: **http://localhost:8080/**

You will land on the login page (`login.html`).

**Demo login**

| Field    | Value              |
|----------|--------------------|
| Username | `admin`            |
| Password | `admin123`         |
| Mail ID  | `admin@gmail.com`  |

If login fails, run: `mysql -u root -p < sql/fix-admin-login.sql`

### 4. Console app (optional)

```bash
mvn compile exec:java -Dexec.mainClass="com.ems.Main"
```

## Web pages

| URL / File       | Purpose                          |
|------------------|----------------------------------|
| `login.html`     | Sign-in form                     |
| `dashboard.html` | Quick links after login          |
| `/employees/`    | List all employees               |
| `/employees/add` | Add employee form                |
| `/employees/view?id=` | View employee details     |
| `/employees/edit?id=` | Update employee           |
| `/employees/salary?id=` | Salary calculation      |

## Project structure

```
src/main/webapp/
├── index.html
├── login.html
├── dashboard.html
├── css/login.css
└── WEB-INF/web.xml
src/main/java/com/ems/
├── servlet/LoginServlet.java
├── servlet/LogoutServlet.java
├── dao/UserDAO.java
├── dao/EmployeeDAO.java
└── ...
sql/schema.sql
```

## Tech stack

| Layer     | Technology              |
|-----------|-------------------------|
| Frontend  | HTML, CSS               |
| Backend   | Java Servlets (Jakarta) |
| Database  | MySQL 8                 |
| Access    | JDBC                    |
| Server    | Jetty (Maven plugin)    |
| Build     | Maven                   |
