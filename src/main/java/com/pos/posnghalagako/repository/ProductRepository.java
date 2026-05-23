package com.pos.posnghalagako.repository;

import com.pos.posnghalagako.model.Product;
import com.pos.posnghalagako.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD operations for the products table.
 * All queries use PreparedStatement to prevent SQL injection.
 */
public class ProductRepository {

    /**
     * Returns all active products, ordered by category then name.
     */
    public List<Product> findAllActive() throws SQLException {
        String sql = "SELECT id, name, description, emoji, price, stock, category, is_active "
                   + "FROM products WHERE is_active = TRUE ORDER BY category, name";
        List<Product> products = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    /**
     * Returns all products (active and inactive).
     */
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT id, name, description, emoji, price, stock, category, is_active "
                   + "FROM products ORDER BY category, name";
        List<Product> products = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    /**
     * Inserts a new product.
     */
    public void save(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, emoji, price, stock, category, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getEmoji());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getCategory());
            ps.setBoolean(7, product.isActive());
            ps.executeUpdate();
        }
    }

    /**
     * Updates an existing product by ID.
     */
    public void update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, emoji = ?, price = ?, "
                   + "stock = ?, category = ?, is_active = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getEmoji());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getCategory());
            ps.setBoolean(7, product.isActive());
            ps.setInt(8, product.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a product by ID.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Decrements stock for a product after a sale.
     */
    public void decrementStock(int productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("emoji"),
                rs.getDouble("price"),
                rs.getInt("stock"),
                rs.getString("category"),
                rs.getBoolean("is_active")
        );
    }
}
