package com.nsu.planningapp.planningapp.model.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.nsu.planningapp.planningapp.model.db.DatabaseConnection;
//
public class DatabaseInitializer {
    // Files with data
    static String DB_TABLES = "src/main/resources/db/create_tables.db";

    public static void fillTables(String[] args) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            createIfNotExistTables(connection);

        } catch (SQLException e) {
            System.err.println("Initialization error: " + e.getMessage());
        }
    }

    private static void createFromDefaultFiles(Connection conn) throws Exception {

    }

    private static void createIfNotExistTables(Connection conn) throws Exception {
        executeSqlScript(conn, DB_TABLES);
    }

    private static void executeSqlScript(Connection conn, String filePath) throws Exception {
        Path path = Paths.get(filePath);
        String content = Files.readString(path);

        List<String> statements = splitSqlStatements(content);

        try (Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                try {
                    stmt.execute(sql);
                } catch (SQLException e) {
                    System.err.println("Error in the statement: " + e.getMessage());
                }
            }
        }
    }

    private static List<String> splitSqlStatements(String content) {
        return Arrays.stream(content.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
