package com.nsu.planningapp.planningapp.model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Deprecated
public class FlexibleCsvImporter {
    public static void importWithStrategy(String filePath, String delimiter,
                                          Connection conn,
                                          String mainInsertSql,
                                          RowProcessingStrategy strategy,
                                          boolean hasHeader) throws Exception {
        try (ImportExecutionContext context = ImportExecutionContext.createContext(filePath, delimiter, conn)) {
            // Установим autoCommit = false, чтобы совершить одно обновление базы за импорт
            conn.setAutoCommit(false);

            // Добавление main statement в контекст
            PreparedStatement mainStmt = context.getOrCreateStatement("main", mainInsertSql);

            // Парсинг строки из CSV
            String[] parts;
            boolean isFirst = true;

            while ((parts = context.parseNextLine()) != null) {
                if (hasHeader && isFirst) {
                    isFirst = false;
                    continue;
                }
                // Парсинг частей, поиск в БД, обновление
                try {
                    strategy.processRow(parts, context);
                    mainStmt.executeUpdate();
                } catch (Exception e) {
                    System.err.println("Error processing row: " + String.join(",", parts));
                    continue; // может throw e;
                }
            }
            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException("Import failed", e);
        }
    }
}
