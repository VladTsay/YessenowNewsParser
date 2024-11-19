package org.example;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DatabaseStorage {
    private static final String DB_URL = "jdbc:sqlite:bot_data.db";

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (chat_id INTEGER PRIMARY KEY)";
            stmt.execute(createUsersTable);

            String createProductsTable = "CREATE TABLE IF NOT EXISTS sent_products (product_id TEXT PRIMARY KEY)";
            stmt.execute(createProductsTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addChatId(Long chatId) {
        String sql = "INSERT OR IGNORE INTO users (chat_id) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, chatId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Set<Long> getAllChatIds() {
        Set<Long> chatIds = new HashSet<>();
        String sql = "SELECT chat_id FROM users";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                chatIds.add(rs.getLong("chat_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatIds;
    }

    public static boolean isProductSent(String productId) {
        String sql = "SELECT 1 FROM sent_products WHERE product_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveProduct(String productId) {
        String sql = "INSERT OR IGNORE INTO sent_products (product_id) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

