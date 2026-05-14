package com.nsu.planningapp;

import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseInitializer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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
            boolean tablesExist = DatabaseInitializer.areTablesPresent(connection);
            System.out.println("\nТекущий статус: " + (tablesExist ? "База данных существует" : "База данных пуста"));
            System.out.println("1 - Создать БД (если есть - пересоздать)");
            System.out.println("2 - Использовать БД (если нет - создать)");
            System.out.println("3 - Выйти");
            System.out.print("\nВыберите 1, 2 или 3: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("\nВНИМАНИЕ! Это действие УДАЛИТ все существующие данные!");
                    System.out.print("Вы уверены? (Y/N): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("Y")) {
                        System.out.print("Пересоздание базы данных... ");
                        DatabaseInitializer.dropTables(connection);
                        createAndFillDatabase(connection);
                        System.out.println("База данных пересоздана и заполнена.");
                    }
                    else
                        System.out.println("\nДействие отменено.");

                    break;
                case "2":
                    if (!tablesExist) {
                        System.out.print("База данных пуста, создаём и заполняем... ");
                        createAndFillDatabase(connection);
                        System.out.println("База данных создана и заполнена.");
                    }
                    else 
                        System.out.println("Используем существующую базу данных.");

                    break;
                case "3":
                    System.out.println("Выход.");
                    return;
                default:
                    System.out.println("Нет такого варианта! Завершение работы.");
                    return;
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
        DatabaseInitializer.createTables(conn);
        DatabaseInitializer.createFromDefaultFiles(conn);
        System.out.println("Соединение установлено, данные добавлены в БД.");
    }
}
