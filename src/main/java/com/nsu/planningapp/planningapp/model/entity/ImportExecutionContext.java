package com.nsu.planningapp.planningapp.model.entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// Level 1: Execution context
@Deprecated
public class ImportExecutionContext implements AutoCloseable {
    private final BufferedReader reader;
    private final Connection connection;
    private final Map<String, PreparedStatement> statements = new HashMap<>();
    private final String delimiter;

    private ImportExecutionContext(String filePath, String delimiter, Connection connection) throws IOException {
        this.reader = new BufferedReader(new FileReader(filePath));
        this.delimiter = delimiter;
        this.connection = connection;
    }

    public static ImportExecutionContext createContext(String filePath, String delimiter, Connection connection)
            throws IOException {
        return new ImportExecutionContext(filePath, delimiter, connection);
    }

    public PreparedStatement getOrCreateStatement(String key, String sql) throws SQLException {
        return statements.computeIfAbsent(key, k -> {
            try {
                return connection.prepareStatement(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String[] parseNextLine() throws IOException {
        String line = this.reader.readLine();
        if (line == null) return null;
        return line.split(delimiter);
    }

    public BufferedReader getReader() { return reader; }

    public Connection getConnection() { return connection;}

    @Override
    public void close() throws Exception {
        reader.close();
        for (PreparedStatement stmt : this.statements.values()) {
            stmt.close();
        }
    }
}
