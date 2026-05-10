package com.nsu.planningapp.planningapp.model.entity;

import com.nsu.planningapp.planningapp.model.csv.CsvExporter;

import java.util.List;
import java.util.Set;


public class BuildingGeneratorApp {
    public static void generate(List<String> settlementsNames, List<String> blueprintsNames) throws Exception {
        try {
            BuildingGenerator generator = new BuildingGenerator(settlementsNames, blueprintsNames);
            Set<BuildingPair> buildingsSet = generator.generateUniqueSet(40);
            String outputPath = "src/main/resources/db/csv/buildings.csv";
            CsvExporter.exportBuildingsToCsv(buildingsSet, outputPath, true);

            System.out.println("\nBuildings exported to: " + outputPath);
        } catch (Exception e) {
            System.err.println("Buildings generation error: " + e.getMessage());
            throw e;
        }
    }
}
