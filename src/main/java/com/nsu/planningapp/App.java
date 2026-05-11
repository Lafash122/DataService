package com.nsu.planningapp;

import com.nsu.planningapp.planningapp.model.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.model.db.DatabaseInitializer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class App {
    // Test: 1) Connection + 2) Filling in tables
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // 0. Установка соединения
            // 1. Создание таблиц
            // 2. Заполнение таблиц данными
            createAndFillDatabase(connection);

            // 3. Запросы.
        } catch (SQLException e) {
            System.err.println("SQLException caught: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException caught: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception caught: " + e.getMessage());
        }
    }

    public static void createAndFillDatabase(Connection conn) throws Exception {
        DatabaseInitializer.createTables();
        DatabaseInitializer.createFromDefaultFiles(conn);
        System.out.println("Соединение установлено, данные добавлены в БД.");
    }
}
