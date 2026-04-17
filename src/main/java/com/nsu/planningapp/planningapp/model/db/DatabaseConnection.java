package com.nsu.planningapp.planningapp.model.db;

import java.sql.DriverManager;

public class DatabaseConnection {
    static String JDBC_URL = "jdbc:postgresql://localhost:5432/socialist_country?currentSchema=public&user=postgres";

    public static void main(String[] args) throws Exception {
        var connection = DriverManager.getConnection(JDBC_URL);
        var statement = connection.createStatement();

        // testing with settlement table creation
        var createTableStatement = "CREATE TABLE SETTLEMENTS (id SERIAL PRIMARY KEY, name VARCHAR(255))";
        statement.execute(createTableStatement);

        System.out.println("Settlement table created");
    }
}
