package com.pos.posnghalagako.repository;

import com.pos.posnghalagako.model.UserAccount;
import com.pos.posnghalagako.util.Database;
import com.pos.posnghalagako.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles user authentication and registration.
 * All queries use PreparedStatement to prevent SQL injection.
 */
public class AuthRepository {

    /**
     * Authenticates a user by username and raw password.
     * Returns the UserAccount if valid, null otherwise.
     */
    public UserAccount authenticate(String username, String rawPassword) throws SQLException {
        String sql = "SELECT id, username, full_name, role, password_hash FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String storedHash = rs.getString("password_hash");
                if (!PasswordUtil.hash(rawPassword).equals(storedHash)) {
                    return null;
                }
                return new UserAccount(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("role")
                );
            }
        }
    }

    /**
     * Checks if a username is already taken.
     */
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Registers a new user. Password is hashed before storage.
     * Returns true on success, false if username already exists.
     */
    public boolean register(String username, String rawPassword, String fullName) throws SQLException {
        if (usernameExists(username)) {
            return false;
        }
        String sql = "INSERT INTO users (username, password_hash, full_name, role) VALUES (?, ?, ?, 'cashier')";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hash(rawPassword));
            ps.setString(3, fullName);
            ps.executeUpdate();
            return true;
        }
    }
}
