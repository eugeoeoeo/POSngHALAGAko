package com.pos.posnghalagako.repository;

import com.pos.posnghalagako.model.CartItem;
import com.pos.posnghalagako.model.Transaction;
import com.pos.posnghalagako.model.TransactionItem;
import com.pos.posnghalagako.util.Database;

import java.sql.*;
import java.util.List;

/**
 * Saves completed transactions and their line items.
 * Uses a single connection with manual commit for atomicity.
 * All queries use PreparedStatement to prevent SQL injection.
 */
public class TransactionRepository {

    private final ProductRepository productRepository = new ProductRepository();

    /**
     * Saves a transaction and all its cart items atomically.
     * Decrements product stock for each item sold.
     */
    public int save(Transaction transaction, List<CartItem> cartItems) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert the transaction header
            String txnSql = "INSERT INTO transactions (cashier_id, total_amount, amount_paid, change_amount) "
                           + "VALUES (?, ?, ?, ?)";
            int transactionId;
            try (PreparedStatement ps = conn.prepareStatement(txnSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, transaction.getCashierId());
                ps.setDouble(2, transaction.getTotalAmount());
                ps.setDouble(3, transaction.getAmountPaid());
                ps.setDouble(4, transaction.getChangeAmount());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    transactionId = keys.getInt(1);
                }
            }

            // 2. Insert each line item
            String itemSql = "INSERT INTO transaction_items "
                           + "(transaction_id, product_id, product_name, quantity, unit_price, subtotal) "
                           + "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(itemSql)) {
                for (CartItem ci : cartItems) {
                    ps.setInt(1, transactionId);
                    ps.setInt(2, ci.getProduct().getId());
                    ps.setString(3, ci.getProduct().getName());
                    ps.setInt(4, ci.getQuantity());
                    ps.setDouble(5, ci.getProduct().getPrice());
                    ps.setDouble(6, ci.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // 3. Decrement stock for each sold product
            String stockSql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
            try (PreparedStatement ps = conn.prepareStatement(stockSql)) {
                for (CartItem ci : cartItems) {
                    ps.setInt(1, ci.getQuantity());
                    ps.setInt(2, ci.getProduct().getId());
                    ps.setInt(3, ci.getQuantity());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return transactionId;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
