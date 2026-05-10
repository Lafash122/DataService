package com.nsu.planningapp.planningapp.model.entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.sql.*;

public class CsvImporter {
    static String delimiter = ";";

    // Resources and settlements (cities, towns, etc.)

    public void importResources(Connection conn, String filePath) throws IOException, SQLException {
        String sql = "INSERT INTO RESOURCES (name) VALUES (?)";
        importResourcesTable(conn, filePath, sql);
    }

    public void importSettlements(Connection conn, String filePath) throws IOException, SQLException {
        String sql = "INSERT INTO SETTLEMENTS (name) VALUES (?)";
        importSettlementsTable(conn, filePath, sql);
    }

    // Blueprints

    public void importBuildingBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO BUILDING_BLUEPRINTS (id, name, blueprint_type, service_life, " +
                "number_of_workdays, daily_water_consumption, daily_energy_consumption) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        importBuildingBlueprints(conn, filePath, sqlQuery);
    }

    public void importTransportBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO TRANSPORT_BLUEPRINTS (id, name, velocity, " +
                "number_of_workdays, type) " +
                "VALUES (?, ?, ?, ?, ?)";
        importTransportBlueprints(conn, filePath, sqlQuery);
    }

    // Subtypes of building blueprints

    public void importFactoryBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO FACTORY_BLUEPRINTS (id, number_of_jobs, " +
                "number_of_jobs_with_higher_education) " +
                "VALUES (?, ?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        importFactoryBlueprints(conn, filePath, sqlQuery, findBlueprintId);
    }

    public void importPublicBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO PUBLIC_FACILITY_BLUEPRINTS (id, number_of_jobs, " +
                "number_of_jobs_with_higher_education, number_of_visitors, number_of_parking_spaces) " +
                "VALUES (?, ?, ?, ?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        importPublicBlueprints(conn, filePath, sqlQuery, findBlueprintId);
    }

    public void importResidentialBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO RESIDENTIAL_BUILDING_BLUEPRINTS (id, number_of_residents, " +
                "quality_of_housing) " +
                "VALUES (?, ?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        importResidentialBlueprints(conn, filePath, sqlQuery, findBlueprintId);
    }

    public void importTemporaryBlueprints(Connection conn, String filePath) throws IOException, SQLException {
        String sqlQuery = "INSERT INTO TEMPORARY_RESIDENCE_BUILDING_BLUEPRINTS (id, number_of_jobs, " +
                "number_of_residents) " +
                "VALUES (?, ?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        importTemporaryBlueprints(conn, filePath, sqlQuery, findBlueprintId);
    }

    // Specimens

    public void importBuildings(Connection conn, String filePath) throws IOException, SQLException {
        String insertSql = "INSERT INTO BUILDINGS VALUES (?, ?)";
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        String findSettlementId = "SELECT id FROM SETTLEMENTS WHERE name = ?";
        importBuildings(conn, filePath, insertSql, findBlueprintId, findSettlementId);
    }

    public void importTransport(Connection conn, String filePath) throws IOException, SQLException  {
        String insertSql = "INSERT INTO TRANSPORT VALUES ?";
        String findBlueprintId = "SELECT id FROM TRANSPORT_BLUEPRINTS WHERE name = ?";
        importTransport(conn, filePath, insertSql, findBlueprintId);
    }

    // Resources for construction / production according to blueprints

    public void importBuildingBlueprintResources(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO BUILDING_CONSTRUCTION_RESOURCES " +
                "(building_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
    }

    public void importTransportBlueprintResources(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM TRANSPORT_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO BUILDING_CONSTRUCTION_RESOURCES " +
                "(transport_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
    }

    // Resources produced and consumed in factories

    public void importConsumedResources(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM FACTORY_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO BUILDING_CONSTRUCTION_RESOURCES " +
                "(factory_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
    }

    public void importProducesBlueprint(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM FACTORY_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO BUILDING_CONSTRUCTION_RESOURCES " +
                "(factory_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
    }

    // Storage resources in buildings

    public void importResourcesStorage(Connection conn, String filePath) throws SQLException, IOException {
        String findBlueprintId = "SELECT id FROM BUILDING_BLUEPRINTS WHERE name = ?";
        String insertSql = "INSERT INTO BUILDING_CONSTRUCTION_RESOURCES " +
                "(building_blueprint_id, resource_id, quantity) VALUES (?, ?, ?)";

        importBlueprintResources(conn, filePath, findBlueprintId, insertSql);
    }


    // Filling in tables containing only the ID and the name of the object
    private static void importSettlementsTable(Connection conn, String filePath, String sqlQuery) throws IOException,
                                                                                                        SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS))
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);
                String name = parts[0];
                pstmt.setString(1, name);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importResourcesTable(Connection conn, String filePath, String sqlQuery) throws IOException,
            SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS))
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);
                String name = parts[0];
                String unit = switch (name.toLowerCase()) {
                    case "вода", "сточные воды" -> "м3";
                    case "электричество" -> "МВт*ч";
                    case "тепло" -> "ГДж";
                    default -> "т";
                };


                pstmt.setString(1, name);
                pstmt.setString(2, unit);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importBuildingBlueprints(Connection conn, String filePath, String sqlQuery) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS))
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int id = Integer.parseInt(parts[1]);
                String blueprint_type = parts[2];
                int service_life = Integer.parseInt(parts[3]);
                int number_of_workdays = Integer.parseInt(parts[4]);
                float daily_water_consumption = Float.parseFloat(parts[5]);
                float daily_energy_consumption = Float.parseFloat(parts[6]);

                pstmt.setString(1, name);
                pstmt.setInt(2, id);
                pstmt.setString(3, blueprint_type);
                pstmt.setInt(4, service_life);
                pstmt.setInt(5, number_of_workdays);
                pstmt.setFloat(6, daily_water_consumption);
                pstmt.setFloat(7, daily_energy_consumption);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importTransportBlueprints(Connection conn, String filePath, String sqlQuery) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery))
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                String id = parts[1];
                float velocity = Float.parseFloat(parts[2]);
                int number_of_workdays = Integer.parseInt(parts[3]);
                String type = parts[4];

                pstmt.setString(1, name);
                pstmt.setString(2, id);
                pstmt.setFloat(3, velocity);
                pstmt.setInt(4, number_of_workdays);
                pstmt.setString(5, type);

                pstmt.executeUpdate();
            }
        }
    }

    // Subtypes of building blueprints

    private static void importFactoryBlueprints(Connection conn, String filePath, String sqlQuery, String findBlueprintId)
            throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId)
        )
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int number_of_jobs = Integer.parseInt(parts[1]);
                int number_of_jobs_with_higher_education = Integer.parseInt(parts[2]);

                findBlueprintStmt.setString(1, name);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                int id;
                if (resultBlueprintSet.next()) {
                    id = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + name);
                    resultBlueprintSet.close();
                    continue;
                }

                pstmt.setInt(1, id);
                pstmt.setInt(2, number_of_jobs);
                pstmt.setInt(3, number_of_jobs_with_higher_education);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importPublicBlueprints(Connection conn, String filePath, String sqlQuery, String findBlueprintId) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId)
        )
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int number_of_jobs = Integer.parseInt(parts[1]);
                int number_of_jobs_with_higher_education = Integer.parseInt(parts[2]);
                int number_of_visitors = Integer.parseInt(parts[3]);
                int number_of_parking_spaces = Integer.parseInt(parts[4]);

                findBlueprintStmt.setString(1, name);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                int id;
                if (resultBlueprintSet.next()) {
                    id = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + name);
                    resultBlueprintSet.close();
                    continue;
                }

                pstmt.setInt(1, id);
                pstmt.setInt(2, number_of_jobs);
                pstmt.setInt(3, number_of_jobs_with_higher_education);
                pstmt.setInt(4, number_of_visitors);
                pstmt.setInt(5, number_of_parking_spaces);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importResidentialBlueprints(Connection conn, String filePath, String sqlQuery, String findBlueprintId) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId)
        )
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int number_of_residents = Integer.parseInt(parts[1]);
                int quality_of_housing = Integer.parseInt(parts[2]);

                findBlueprintStmt.setString(1, name);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                int id;
                if (resultBlueprintSet.next()) {
                    id = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + name);
                    resultBlueprintSet.close();
                    continue;
                }

                pstmt.setInt(1, id);
                pstmt.setInt(2, number_of_residents);
                pstmt.setInt(3, quality_of_housing);

                pstmt.executeUpdate();
            }
        }
    }

    private static void importTemporaryBlueprints(Connection conn, String filePath, String sqlQuery, String findBlueprintId) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId)
        )
        {
            String line;
            boolean isHeader = true;
            while((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String name = parts[0];
                int number_of_jobs = Integer.parseInt(parts[1]);
                int number_of_residents = Integer.parseInt(parts[2]);

                findBlueprintStmt.setString(1, name);
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                int id;
                if (resultBlueprintSet.next()) {
                    id = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + name);
                    resultBlueprintSet.close();
                    continue;
                }

                pstmt.setInt(1, id);
                pstmt.setInt(2, number_of_jobs);
                pstmt.setInt(3, number_of_residents);

                pstmt.executeUpdate();
            }
        }
    }

    // Specimens

    private static void importBuildings(Connection conn, String filePath,
                                        String insertSql, String findBlueprintId, String findSettlementId)
            throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId);
             PreparedStatement findSettlementStmt = conn.prepareStatement(findSettlementId);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)
        ) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);

                String settlementName = parts[0];
                String blueprintName = parts[1];

                int settlementId;
                int blueprintId;

                ResultSet resultSettlementSet = findSettlementStmt.executeQuery();
                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                if (resultBlueprintSet.next()) {
                    blueprintId = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + blueprintName);
                    resultBlueprintSet.close();
                    resultSettlementSet.close();
                    continue;
                }

                if (resultSettlementSet.next()) {
                    settlementId = resultSettlementSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Resource not found: " + settlementName);
                    resultBlueprintSet.close();
                    resultSettlementSet.close();
                    continue;
                }

                insertStmt.setInt(1, blueprintId);
                insertStmt.setInt(2, settlementId);
                insertStmt.executeUpdate();

                resultBlueprintSet.close();
                resultSettlementSet.close();
            }
        }
    }

    private static void importTransport(Connection conn, String filePath,
                                        String insertSql, String findBlueprintId) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)
        ) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(delimiter);
                String blueprintName = parts[0];
                int blueprintId;

                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();

                if (resultBlueprintSet.next()) {
                    blueprintId = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + blueprintName);
                    resultBlueprintSet.close();
                    continue;
                }

                insertStmt.setInt(1, blueprintId);
                insertStmt.executeUpdate();

                resultBlueprintSet.close();
            }
        }
    }

    // Resources for construction according to blueprints

    private static void importBlueprintResources(Connection conn,
                                                 String filePath,
                                                 String findBlueprintId,
                                                 String insertSql) throws IOException, SQLException {
        String findResourceSql = "SELECT id FROM RESOURCES WHERE name = ?";
        try (BufferedReader bf = new BufferedReader(new FileReader(filePath));
            PreparedStatement findResourceStmt = conn.prepareStatement(findResourceSql);
            PreparedStatement findBlueprintStmt = conn.prepareStatement(findBlueprintId);
            PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            String line;
            boolean isHeader = true;
            while((line = bf.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(delimiter);
                String blueprintName = parts[0];
                String resourceName = parts[1];
                int quantity = Integer.parseInt(parts[2]);

                // Requesting a resource ID from the database
                findBlueprintStmt.setString(1, blueprintName);
                findResourceStmt.setString(1, resourceName);

                ResultSet resultBlueprintSet = findBlueprintStmt.executeQuery();
                ResultSet resultResourceSet = findResourceStmt.executeQuery();

                int blueprintId;
                int resourceId;

                if (resultBlueprintSet.next()) {
                    blueprintId = resultBlueprintSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Blueprint not found: " + blueprintName);
                    resultBlueprintSet.close();
                    resultResourceSet.close();
                    continue;
                }

                if (resultResourceSet.next()) {
                    resourceId = resultResourceSet.getInt("id");
                } else {
                    // TODO: заменить на исключение
                    System.err.println("Resource not found: " + resourceName);
                    resultBlueprintSet.close();
                    resultResourceSet.close();
                    continue;
                }

                insertStmt.setInt(1, blueprintId);
                insertStmt.setInt(2,resourceId);
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();

                resultBlueprintSet.close();
                resultResourceSet.close();
            }
        }
    }
}
