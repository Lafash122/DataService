package com.nsu.planningapp.planningapp.model.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

// For exporting data into CSV files (like settlements, buildings, etc.)
public class CsvExporter {
    public static void exportToCsv(Collection<String> items, String filePath) throws IOException {
        exportToCsv(items, filePath, false);
    }

    public static void exportToCsv(Collection<String> items, String filePath, boolean includeHeader)
            throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            if (includeHeader) {
                writer.write("Название города");
                writer.newLine();
            }

            for (String item : items) {
                writer.write(item);
                writer.newLine();
            }
        }
    }
}