package com.nsu.planningapp.planningapp.model.entity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class CsvDataLoader {
    static String delimiter = ";";

    public void loadResources(Connection conn, String filePath) throws Exception {
        FlexibleCsvImporter.importWithStrategy(filePath, delimiter, conn,
                "INSERT INTO RESOURCES (name) VALUES (?)",
                (parts, context) -> {
                    context.getOrCreateStatement("main", "").setString(1, parts[0]);
                }, true);
    }
}
