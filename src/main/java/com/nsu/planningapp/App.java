package com.nsu.planningapp;

import com.nsu.planningapp.planningapp.model.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.model.db.DatabaseInitializer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {
    // Test: 1) Connection + 2) Filling in tables
    public static void main(String[] args) {
        try {
            // 0. Установка соединения
            Connection connection = DatabaseConnection.getConnection();
            // 1. Создание таблиц
            DatabaseInitializer.createTables();
            // 2. Заполнение таблиц данными
            DatabaseInitializer.createFromDefaultFiles(connection);
            System.out.println("Успешно завершено!");
        } catch (SQLException e) {
            System.err.println("SQLException caught: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException caught: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception caught: " + e.getMessage());
        }
    }
}
