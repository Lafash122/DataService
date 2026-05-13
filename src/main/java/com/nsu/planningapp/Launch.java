package com.nsu.planningapp;

import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseInitializer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;


public class Launch {
    // Test: 1) Connection + 2) Filling in tables
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // 0. Установка соединения
            // 1. Создание таблиц
            // 2. Заполнение таблиц данными

            Scanner scanner = new Scanner(System.in);
            System.out.print("Вы ходите подключиться к СУБД, создать и заполнить БД? ([Y]es/[N]o)): ");
            String key = scanner.nextLine();
            if (key.equals("Y")) {
                System.out.print("Запрос принят пользователем. Подключаюсь к СУБД...");
                createAndFillDatabase(connection);
            } else if (key.equals("N")) {
                System.out.print("Запрос отклонен пользователем.");
            } else {
                System.out.print("Нет такого варианта!");
            }
            System.out.print("Завершение работы. P. S. 12 запросы - в QueryService!");
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
