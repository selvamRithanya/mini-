package com.ems.dao;

import com.ems.db.DatabaseConnection;
import com.ems.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO {

    private static final String AUTH_QUERY =
            "SELECT id, username, password, email FROM users "
                    + "WHERE LOWER(username) = LOWER(?) AND password = ? AND LOWER(email) = LOWER(?)";

    public Optional<User> authenticate(String username, String password, String email)
            throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(AUTH_QUERY)) {

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            ps.setString(3, email.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email")));
                }
            }
        }
        return Optional.empty();
    }
}
