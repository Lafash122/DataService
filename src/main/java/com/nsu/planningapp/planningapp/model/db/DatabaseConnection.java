package com.nsu.planningapp.planningapp.model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    static String JDBC_URL = "jdbc:postgresql://localhost:5432/socialist_country" +
            "?currentSchema=public" +
            "&user" +
            "&password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Connection closure error: " + e.getMessage());
            }
        }
    }
}
