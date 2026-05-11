package com.nsu.planningapp.planningapp.model.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.nsu.planningapp.planningapp.model.csv.CsvReader;
import com.nsu.planningapp.planningapp.model.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.model.entity.BuildingGeneratorApp;
import com.nsu.planningapp.planningapp.model.entity.CsvImporter;
import com.nsu.planningapp.planningapp.model.entity.SettlementNameGeneratorApp;
import com.nsu.planningapp.planningapp.model.entity.TransportGeneratorApp;
import com.nsu.planningapp.planningapp.model.utils.DatabaseUtils;

//
public class DatabaseInitializer {
    // Files with data
    static String DB_TABLES = "src/main/resources/db/create_tables.db";

    static String CSV_FOLDER = "src/main/resources/db/csv/";
    static String DELIMITER = ";";

    // 1. Settlements + resources
    static String GENERATEDSETTLEMENTS = CSV_FOLDER + "generatedsettlements.csv";
    static String RESOURCES = CSV_FOLDER + "resources.csv";

    // 2. Blueprints
    static String BBLUEPRINT = CSV_FOLDER + "bblueprint.csv";
    static String TBLUEPRINT = CSV_FOLDER + "tblueprint.csv";

    // 3. Specific blueprints info
    static String FACTORYBLUEPRINT = CSV_FOLDER + "factoryblueprint.csv";
    static String GOVBLUEPRINT = CSV_FOLDER + "govblueprint.csv";
    static String LIVINGBLUEPRINT = CSV_FOLDER + "livingblueprint.csv";
    static String TEMPBLUEPRINT = CSV_FOLDER + "tempblueprint.csv";

    // 4. Resources according to blueprints
    static String RESOURCES4BBLUEPRINT = CSV_FOLDER + "resources4bblueprint.csv";
    static String RESOURCES4TBLUEPRINT = CSV_FOLDER + "resources4tblueprint.csv";
    static String RESOURCESCONSUME = CSV_FOLDER + "resourcesconsume.csv";
    static String RESOURCESKEEP = CSV_FOLDER + "resourceskeep.csv";
    static String RESOURCESPRODUCE = CSV_FOLDER + "resourcesproduce.csv";

    // 5. Created buildings & transport
    static String BUILDINGS = CSV_FOLDER + "buildings.csv";
    static String TRANSPORT = CSV_FOLDER + "transport.csv";

    public static void createTables() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            createIfNotExistTables(connection);

        } catch (SQLException e) {
            System.err.println("Initialization error: " + e.getMessage());
        }
    }

    // Инициализация в 4 этапа по стандарту
    public static void createFromDefaultFiles(Connection conn) throws Exception {
        try {
            dataGeneration();
            fillTables(conn);
        } catch (Exception e) {
            throw new Exception("Filling in database exception: " + e.getMessage());
        }
    }

    private static void dataGeneration() throws Exception {
        // 0. ОБРАБОТКА: города, здания, транспорт
        // -- Названия нас пунктов - в файл GENERATEDSETTLEMENTS
        SettlementNameGeneratorApp.generate(GENERATEDSETTLEMENTS);

        // -- Здания - на основе чертежей и нас пунктов
        List<String> settlementsNames = CsvReader.readNamesFromCsv(GENERATEDSETTLEMENTS, DELIMITER, true);
        List<String> bblueprintsNames = CsvReader.readNamesFromCsv(BBLUEPRINT, DELIMITER, true);

        BuildingGeneratorApp.generate(settlementsNames, bblueprintsNames, BUILDINGS);

        // -- Транспорт - на основе чертежей
        List<String> tblueprintsNames = CsvReader.readNamesFromCsv(TBLUEPRINT, DELIMITER, true);
        TransportGeneratorApp.generate(tblueprintsNames, TRANSPORT);
    }

    private static void fillTables(Connection conn) throws Exception {
        CsvImporter importer = new CsvImporter();

        // 1
        importer.importResources(conn, RESOURCES);
        importer.importSettlements(conn, GENERATEDSETTLEMENTS);

        // 2
        importer.importBuildingBlueprints(conn, BBLUEPRINT);
        importer.importTransportBlueprints(conn, TBLUEPRINT);

        // 3
        importer.importFactoryBlueprints(conn, FACTORYBLUEPRINT);
        importer.importPublicBlueprints(conn, GOVBLUEPRINT);
        importer.importResidentialBlueprints(conn, LIVINGBLUEPRINT);
        importer.importTemporaryBlueprints(conn, TEMPBLUEPRINT);

        // 4
        importer.importBuildings(conn, BUILDINGS);
        importer.importTransport(conn, TRANSPORT);

        // 5
        importer.importBuildingBlueprintResources(conn, RESOURCES4BBLUEPRINT);
        importer.importTransportBlueprintResources(conn, RESOURCES4TBLUEPRINT);
        importer.importConsumedResources(conn, RESOURCESCONSUME);
        importer.importProducesBlueprint(conn, RESOURCESPRODUCE);
        importer.importResourcesStorage(conn, RESOURCESKEEP);
    }

    private static void createIfNotExistTables(Connection conn) throws Exception {
        DatabaseUtils.executeSqlScript(conn, DB_TABLES);
    }

    private static void executeSqlScript(Connection conn, String filePath) throws Exception {
        Path path = Paths.get(filePath);
        String content = Files.readString(path);

        List<String> statements = splitSqlStatements(content);

        try (Statement stmt = conn.createStatement()) {
            for (String sql : statements) {
                //System.out.println(sql); -- debug
                try {
                    stmt.execute(sql);
                } catch (SQLException e) {
                    System.err.println("Error in the statement: " + e.getMessage());
                }
            }
        }
    }

    private static List<String> splitSqlStatements(String content) {
        return Arrays.stream(content.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
