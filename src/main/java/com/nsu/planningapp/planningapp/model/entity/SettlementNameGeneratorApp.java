package com.nsu.planningapp.planningapp.model.entity;

import com.nsu.planningapp.planningapp.model.csv.CsvExporter;

import java.util.Set;

public class SettlementNameGeneratorApp {
    public static void generate(String[] args) throws Exception {
        try {
            SettlementNameGenerator generator = new SettlementNameGenerator(
                    SettlementNameDictionary.getRussianPrefixes(),
                    SettlementNameDictionary.getRussianPostfixes(),
                    SettlementNameDictionary.getRussianConnectors()
            );

            Set<String> settlementNames = generator.generateUniqueSet(10);

            //System.out.println("Generated " + settlementNames.size() + " settlement names:");
            //cityNames.forEach(System.out::println);

            String outputPath = "src/main/resources/db/csv/generatedsettlements.csv";
            CsvExporter.exportToCsv(settlementNames, outputPath, true);

            System.out.println("\nSettlements exported to: " + outputPath);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
}