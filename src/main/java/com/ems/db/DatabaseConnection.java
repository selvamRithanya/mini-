package com.ems.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            props.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        if (url == null || url.isBlank()) {
            throw new IllegalStateException("Property 'db.url' is not set in db.properties");
        }

        final int attempts = 3;
        SQLException lastEx = null;
        for (int i = 1; i <= attempts; i++) {
            try {
                return DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                lastEx = e;
                System.err.println("Database connection attempt " + i + " failed: " + e.getMessage());
                // If this was the last attempt, break and throw below
                if (i == attempts) break;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        throw new SQLException("Unable to connect to database after " + attempts + " attempts: " +
                (lastEx == null ? "unknown error" : lastEx.getMessage()), lastEx);
    }
}
