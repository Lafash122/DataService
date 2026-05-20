package com.nsu.planningapp.planningapp.service;

import com.nsu.planningapp.planningapp.dto.*;
import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryService {
    //TODO: возможно, получить сет типов зданий, которые есть в БД (bblueprint)

    // 1
    public int getTotalResidentCapacity(Integer settlementId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(rb.number_of_residents), 0) " +
                    "FROM RESIDENTIAL_BUILDING_BLUEPRINTS rb " +
                    "JOIN BUILDINGS b ON b.blueprint = rb.id";

        if (settlementId != null)
            sql += " WHERE b.settlement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (settlementId != null)
                stmt.setInt(1, settlementId);

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 2
    public List<BuildingDto> getBuildingsByType(String blueprintType, String settlementName) throws SQLException {
        String sql;
        if (settlementName == null || settlementName.isEmpty()) {
            sql = "SELECT b.id AS id, s.name AS settlement_name, bb.name AS building_name, bb.blueprint_type " +
                    "FROM BUILDINGS b " +
                    "JOIN SETTLEMENTS s ON b.settlement = s.id " +
                    "JOIN BUILDING_BLUEPRINTS bb ON b.blueprint = bb.id " +
                    "WHERE bb.blueprint_type = ? " +
                    "ORDER BY s.name, bb.name";
        } else {
            sql = "SELECT b.id AS id, s.name AS settlement_name, bb.name AS building_name, bb.blueprint_type " +
                    "FROM BUILDINGS b " +
                    "JOIN SETTLEMENTS s ON b.settlement = s.id " +
                    "JOIN BUILDING_BLUEPRINTS bb ON b.blueprint = bb.id " +
                    "WHERE bb.blueprint_type = ? AND s.name = ? " +
                    "ORDER BY bb.name";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, blueprintType);
            if (settlementName != null && !settlementName.isEmpty()) {
                stmt.setString(2, settlementName);
            }

            ResultSet rs = stmt.executeQuery();
            List<BuildingDto> buildings = new ArrayList<>();
            while (rs.next()) {
                BuildingDto dto = new BuildingDto(rs.getInt("id"),
                        rs.getString("settlement_name"),
                        rs.getString("building_name"),
                        rs.getString("blueprint_type"));
                buildings.add(dto);
            }
            return buildings;
        }
    }

    // 3
    public int getMaxResourceProduction(Integer resourceId, Integer cityId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(rp.quantity), 0) FROM AMOUNT_OF_RESOURCES_PRODUCED rp " +
                    "JOIN BUILDINGS b ON b.blueprint = rp.factory_blueprint_id " +
                    "WHERE rp.resource_id = ?";
        if (cityId != null)
            sql += " AND b.settlement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, resourceId);
            if (cityId != null) {
                stmt.setInt(2, cityId);
            }

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
/*
    public int getMaxResourceProduction(String resourceName, Integer cityId) throws SQLException {
        String sql;
        if (cityId == null) {
            sql = "SELECT COALESCE(SUM(rp.quantity), 0) FROM AMOUNT_OF_RESOURCES_PRODUCED rp " +
                    "JOIN RESOURCES r ON rp.resource_id = r.id " +
                    "WHERE r.name = ?";
        } else {
            sql = "SELECT COALESCE(SUM(rp.quantity), 0) FROM AMOUNT_OF_RESOURCES_PRODUCED rp " +
                    "JOIN BUILDINGS b ON b.blueprint = rp.factory_blueprint_id " +
                    "JOIN RESOURCES r ON rp.resource_id = r.id " +
                    "WHERE r.name = ? AND b.settlement = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resourceName);
            if (cityId != null) {
                stmt.setInt(2, cityId);
            }

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
*/
    // 4
    public int getMaxResourceConsumption(String resourceName, Integer settlementId) throws SQLException {
        String sql;
        if (settlementId == null) {
            sql = "SELECT COALESCE(SUM(rc.quantity), 0) FROM AMOUNT_OF_RESOURCES_CONSUMED rc " +
                    "JOIN RESOURCES r ON rc.resource_id = r.id " +
                    "WHERE r.name = ?";
        } else {
            sql = "SELECT COALESCE(SUM(rc.quantity), 0) FROM AMOUNT_OF_RESOURCES_CONSUMED rc " +
                    "JOIN BUILDINGS b ON b.blueprint = rc.factory_blueprint_id " +
                    "JOIN RESOURCES r ON rc.resource_id = r.id " +
                    "WHERE r.name = ? AND b.settlement = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resourceName);
            if (settlementId != null) {
                stmt.setInt(2, settlementId);
            }

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // 5
    public JobsReportDto getJobsCount(Integer settlementId, Integer buildingId) throws SQLException {
        String sql;
    
        if (buildingId != null) {
            sql = """
                SELECT 
                    COALESCE(SUM(fb.number_of_jobs), 0) + 
                    COALESCE(SUM(pf.number_of_jobs), 0) + 
                    COALESCE(SUM(tr.number_of_jobs), 0) AS total_jobs,
                    COALESCE(SUM(fb.number_of_jobs_with_higher_education), 0) + 
                    COALESCE(SUM(pf.number_of_jobs_with_higher_education), 0) + 
                    COALESCE(SUM(tr.number_of_jobs_with_higher_education), 0) AS total_higher_edu_jobs
                FROM BUILDINGS b
                LEFT JOIN FACTORY_BLUEPRINTS fb ON b.blueprint = fb.id
                LEFT JOIN PUBLIC_FACILITY_BLUEPRINTS pf ON b.blueprint = pf.id
                LEFT JOIN TEMPORARY_RESIDENCE_BUILDING_BLUEPRINTS tr ON b.blueprint = tr.id
                WHERE b.id = ?
                """;
        }
        else if (settlementId != null) {
            sql = """
                SELECT 
                    COALESCE(SUM(fb.number_of_jobs), 0) + 
                    COALESCE(SUM(pf.number_of_jobs), 0) + 
                    COALESCE(SUM(tr.number_of_jobs), 0) AS total_jobs,
                    COALESCE(SUM(fb.number_of_jobs_with_higher_education), 0) + 
                    COALESCE(SUM(pf.number_of_jobs_with_higher_education), 0) + 
                    COALESCE(SUM(tr.number_of_jobs_with_higher_education), 0) AS total_higher_edu_jobs
                FROM BUILDINGS b
                LEFT JOIN FACTORY_BLUEPRINTS fb ON b.blueprint = fb.id
                LEFT JOIN PUBLIC_FACILITY_BLUEPRINTS pf ON b.blueprint = pf.id
                LEFT JOIN TEMPORARY_RESIDENCE_BUILDING_BLUEPRINTS tr ON b.blueprint = tr.id
                WHERE b.settlement = ?
                """;
        }
        else {
            sql = """
                SELECT 
                    COALESCE(SUM(fb.number_of_jobs), 0) + 
                    COALESCE(SUM(pf.number_of_jobs), 0) + 
                    COALESCE(SUM(tr.number_of_jobs), 0) AS total_jobs,
                    COALESCE(SUM(fb.number_of_jobs_with_higher_education), 0) + 
                    COALESCE(SUM(pf.number_of_jobs_with_higher_education), 0) + 
                    COALESCE(SUM(tr.number_of_jobs_with_higher_education), 0) AS total_higher_edu_jobs
                FROM BUILDINGS b
                LEFT JOIN FACTORY_BLUEPRINTS fb ON b.blueprint = fb.id
                LEFT JOIN PUBLIC_FACILITY_BLUEPRINTS pf ON b.blueprint = pf.id
                LEFT JOIN TEMPORARY_RESIDENCE_BUILDING_BLUEPRINTS tr ON b.blueprint = tr.id
                """;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        
            if (buildingId != null)
                stmt.setInt(1, buildingId);
            else if (settlementId != null)
                stmt.setInt(1, settlementId);
        
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return new JobsReportDto(rs.getInt("total_jobs"), rs.getInt("total_higher_edu_jobs"));

            return new JobsReportDto(0, 0);
        }
    }

    // 6
    public double getTotalResourceStorage(String resourceName, Integer settlementId) throws SQLException {
        String sql;
        if (settlementId == null) {
            sql = "SELECT COALESCE(SUM(ss.quantity), 0) AS total_storage " +
                  "FROM RESOURCE_STORAGE_SIZES ss " +
                  "JOIN RESOURCES r ON ss.resource_id = r.id " +
                  "WHERE r.name = ?";
        }
        else {
            sql = "SELECT COALESCE(SUM(ss.quantity), 0) AS total_storage " +
                  "FROM RESOURCE_STORAGE_SIZES ss " +
                  "JOIN RESOURCES r ON ss.resource_id = r.id " +
                  "JOIN BUILDINGS b ON ss.building_blueprint_id = b.blueprint " +
                  "WHERE r.name = ? AND b.settlement = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resourceName);
            if (settlementId != null)
                 stmt.setInt(2, settlementId);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getDouble("total_storage") : 0.0;
        }
    }

    // 7
    public ConstructionDetailsDto getBuildingConstructionCost(List<Integer> blueprintIds) throws SQLException {
        if (blueprintIds == null || blueprintIds.isEmpty())
            return new ConstructionDetailsDto(List.of(), 0);

        String placeholders = String.join(",", Collections.nCopies(blueprintIds.size(), "?"));

        String sqlResources = """
            SELECT r.name AS resource_name, COALESCE(SUM(bcr.quantity), 0) AS total_quantity
            FROM BUILDING_BLUEPRINTS bb
            LEFT JOIN BUILDING_CONSTRUCTION_RESOURCES bcr ON bb.id = bcr.building_blueprint_id
            LEFT JOIN RESOURCES r ON bcr.resource_id = r.id
            WHERE bb.id IN (%s)
            GROUP BY r.name
            """.formatted(placeholders);

        String sqlWorkdays = """
            SELECT COALESCE(SUM(bb.number_of_workdays), 0) AS total_workdays
            FROM BUILDING_BLUEPRINTS bb
            WHERE bb.id IN (%s)
            """.formatted(placeholders);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtResources = conn.prepareStatement(sqlResources);
             PreparedStatement stmtWorkdays = conn.prepareStatement(sqlWorkdays)) {

            for (int i = 0; i < blueprintIds.size(); i++) {
                stmtResources.setInt(i + 1, blueprintIds.get(i));
                stmtWorkdays.setInt(i + 1, blueprintIds.get(i));
            }

            List<ResourceRequirementDto> resources = new ArrayList<>();
            try (ResultSet rsResources = stmtResources.executeQuery()) {
                while (rsResources.next()) {
                    String name = rsResources.getString("resource_name");
                    if (name != null) { // если ресурсов нет — пропускаем
                        double qty = rsResources.getDouble("total_quantity");
                        resources.add(new ResourceRequirementDto(name, qty));
                    }
                }
            }

            int totalWorkdays = 0;
            try (ResultSet rsWorkdays = stmtWorkdays.executeQuery()) {
                if (rsWorkdays.next())
                    totalWorkdays = rsWorkdays.getInt("total_workdays");
            }

            return new ConstructionDetailsDto(resources, totalWorkdays);
        }
    }

    // 8
    public ConstructionDetailsDto getTransportConstructionCost(List<Integer> transportBlueprintIds) throws SQLException {
        if (transportBlueprintIds == null || transportBlueprintIds.isEmpty())
            return new ConstructionDetailsDto(List.of(), 0);

        String placeholders = String.join(",", Collections.nCopies(transportBlueprintIds.size(), "?"));

        String sqlResources = """
            SELECT r.name AS resource_name, COALESCE(SUM(tcr.quantity), 0) AS total_quantity
            FROM TRANSPORT_BLUEPRINTS tb
            LEFT JOIN TRANSPORT_CONSTRUCTION_RESOURCES tcr ON tb.id = tcr.transport_blueprint_id
            LEFT JOIN RESOURCES r ON tcr.resource_id = r.id
            WHERE tb.id IN (%s)
            GROUP BY r.name
            """.formatted(placeholders);

        String sqlWorkdays = """
            SELECT COALESCE(SUM(tb.number_of_workdays), 0) AS total_workdays
            FROM TRANSPORT_BLUEPRINTS tb
            WHERE tb.id IN (%s)
            """.formatted(placeholders);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtResources = conn.prepareStatement(sqlResources);
             PreparedStatement stmtWorkdays = conn.prepareStatement(sqlWorkdays)) {

            for (int i = 0; i < transportBlueprintIds.size(); i++) {
                stmtResources.setInt(i + 1, transportBlueprintIds.get(i));
                stmtWorkdays.setInt(i + 1, transportBlueprintIds.get(i));
            }

            List<ResourceRequirementDto> resources = new ArrayList<>();
            try (ResultSet rsResources = stmtResources.executeQuery()) {
                while (rsResources.next()) {
                    String name = rsResources.getString("resource_name");
                    if (name != null)
                        resources.add(new ResourceRequirementDto(name, rsResources.getDouble("total_quantity")));
                }
            }

            int totalWorkdays = 0;
            try (ResultSet rsWorkdays = stmtWorkdays.executeQuery()) {
                if (rsWorkdays.next())
                    totalWorkdays = rsWorkdays.getInt("total_workdays");
            }

            return new ConstructionDetailsDto(resources, totalWorkdays);
        }
    }

    // 9
    public List<BuildingInfoDto> getBuildingsList(Integer settlementId) throws SQLException {
        String sql = "SELECT b.id, bb.name AS blueprint_name, s.name AS settlement_name, bb.blueprint_type " +
                "FROM BUILDINGS b " +
                "JOIN BUILDING_BLUEPRINTS bb ON b.blueprint = bb.id " +
                "JOIN SETTLEMENTS s ON b.settlement = s.id " +
                "WHERE (? IS NULL OR b.settlement = ?) " +
                "ORDER BY s.name, bb.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (settlementId == null) {
                stmt.setNull(1, Types.INTEGER);
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(1, settlementId);
                stmt.setInt(2, settlementId);
            }

            ResultSet rs = stmt.executeQuery();
            List<BuildingInfoDto> buildings = new ArrayList<>();
            while (rs.next()) {
                buildings.add(new BuildingInfoDto(
                        rs.getInt("id"),
                        rs.getString("blueprint_name"),
                        rs.getString("settlement_name"),
                        rs.getString("blueprint_type")
                ));
            }
            return buildings;
        }
    }

    // 10
    public double getDaysToFillStorage(String resourceName, Integer settlementId) throws SQLException {
        String sql;
        if (settlementId == null) {
            // По всей стране
            sql = "SELECT " +
                    "   (SELECT COALESCE(SUM(ss.quantity), 0) FROM RESOURCE_STORAGE_SIZES ss " +
                    "    JOIN RESOURCES r ON ss.resource_id = r.id WHERE r.name = ?) / " +
                    "   NULLIF((SELECT COALESCE(SUM(arp.quantity), 0) FROM AMOUNT_OF_RESOURCES_PRODUCED arp " +
                    "          JOIN RESOURCES r ON arp.resource_id = r.id WHERE r.name = ?), 0) " +
                    "AS days_to_fill";
        } else {
            // В конкретном городе
            sql = "SELECT " +
                    "   (SELECT COALESCE(SUM(ss.quantity), 0) FROM RESOURCE_STORAGE_SIZES ss " +
                    "    JOIN RESOURCES r ON ss.resource_id = r.id " +
                    "    JOIN BUILDINGS b ON ss.building_blueprint_id = b.blueprint " +
                    "    WHERE r.name = ? AND b.settlement = ?) / " +
                    "   NULLIF((SELECT COALESCE(SUM(arp.quantity), 0) FROM AMOUNT_OF_RESOURCES_PRODUCED arp " +
                    "          JOIN RESOURCES r ON arp.resource_id = r.id " +
                    "          JOIN FACTORY_BLUEPRINTS fb ON arp.factory_blueprint_id = fb.id " +
                    "          JOIN BUILDINGS b ON fb.id = b.blueprint " +
                    "          WHERE r.name = ? AND b.settlement = ?), 0) " +
                    "AS days_to_fill";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resourceName);
            if (settlementId == null) {
                stmt.setString(2, resourceName);
            } else {
                stmt.setInt(2, settlementId);
                stmt.setString(3, resourceName);
                stmt.setInt(4, settlementId);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double days = rs.getDouble("days_to_fill");
            // Если в БД значение NULL (производство = 0), возвращаем бесконечность
                if (rs.wasNull()) {
                    return Double.POSITIVE_INFINITY;
                }
                return days;
            }
            return Double.POSITIVE_INFINITY;
        }
    }

    // 11
    public int getTotalParkingSpaces(Integer settlementId, List<Integer> buildingIds) throws SQLException {
        String sql;
        if (buildingIds != null && !buildingIds.isEmpty()) {
            String placeholders = String.join(",", Collections.nCopies(buildingIds.size(), "?"));
            sql = "SELECT COALESCE(SUM(pf.number_of_parking_spaces), 0) AS total_parking " +
                    "FROM BUILDINGS b " +
                    "JOIN PUBLIC_FACILITY_BLUEPRINTS pf ON b.blueprint = pf.id " +
                    "WHERE b.id IN (" + placeholders + ")";
        } else if (settlementId != null) {
            sql = "SELECT COALESCE(SUM(pf.number_of_parking_spaces), 0) AS total_parking " +
                    "FROM BUILDINGS b " +
                    "JOIN PUBLIC_FACILITY_BLUEPRINTS pf ON b.blueprint = pf.id " +
                    "WHERE b.settlement = ?";
        } else {
            sql = "SELECT COALESCE(SUM(pf.number_of_parking_spaces), 0) AS total_parking " +
                    "FROM BUILDINGS b " +
                    "JOIN PUBLIC_FACILITY_BLUEPRINTS pf ON b.blueprint = pf.id";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int paramIndex = 1;
            if (buildingIds != null && !buildingIds.isEmpty()) {
                for (int id : buildingIds) {
                    stmt.setInt(paramIndex++, id);
                }
            } else if (settlementId != null) {
                stmt.setInt(1, settlementId);
            }
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("total_parking") : 0;
        }
    }

    // 12
    public double getMaxStorageInNonStorageBuildings(String resourceName) throws SQLException {
        String sql = "SELECT MAX(ss.quantity) AS max_storage " +
                 "FROM RESOURCE_STORAGE_SIZES ss " +
                 "JOIN RESOURCES r ON ss.resource_id = r.id " +
                 "JOIN BUILDING_BLUEPRINTS bb ON ss.building_blueprint_id = bb.id " +
                 "WHERE r.name = ? " +
                 "  AND bb.blueprint_type != 'хранилище'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, resourceName);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getDouble("max_storage") : 0.0;
        }
    }
}
