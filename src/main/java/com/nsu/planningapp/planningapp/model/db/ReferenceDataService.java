package com.nsu.planningapp.planningapp.model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReferenceDataService {
    // Settlements
    public static List<String> getAllSettlements() throws SQLException {
        String sql = "SELECT name FROM SETTMELENTS ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<String> cities = new ArrayList<>();
            while (rs.next()) {
                cities.add(rs.getString("name"));
            }
            return cities;
        }
    }

    public Integer getSettlementId(String cityName) throws SQLException {
        String sql = "SELECT id FROM SETTLEMENTS WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cityName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    // Transport blueprints

    public static List<String> getAllTransportNames() throws SQLException {
        String sql = "SELECT name FROM TRANSPORT_BLUEPRINTS ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<String> transports = new ArrayList<>();
            while (rs.next()) {
                transports.add(rs.getString("name"));
            }
            return transports;
        }
    }

    public Integer getTransportBlueprintId(String transportName) throws SQLException {
        String sql = "SELECT id FROM TRANSPORT_BLUEPRINTS WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transportName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    // Resources

    public static List<String> getAllResourcesNames() throws SQLException {
        String sql = "SELECT name FROM RESOURCRES ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<String> resources = new ArrayList<>();
            while (rs.next()) {
                resources.add(rs.getString("name"));
            }
            return resources;
        }
    }

    public Integer getResourceId(String resourceName) throws SQLException {
        String sql = "SELECT id FROM RESOURCES WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resourceName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }

    // Building blueprint types
    public List<String> getAllBlueprintTypes() throws SQLException {
        String sql = "SELECT DISTINCT blueprint_type FROM BUILDING_BLUEPRINTS ORDER BY blueprint_type";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<String> types = new ArrayList<>();
            while (rs.next()) {
                types.add(rs.getString("blueprint_type"));
            }
            return types;
        }
    }

    // Constructed buildings
    public List<String> getAllBuildings() throws SQLException {
        String sql = "SELECT id, name FROM BUILDINGS ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<String> buildings = new ArrayList<>();
            while (rs.next()) {
                buildings.add(rs.getString("name"));
            }
            return buildings;
        }
    }

    public List<String> getBuildingsBySettlement(String settlementName) throws SQLException {
        String sql = "SELECT b.name FROM BUILDINGS b " +
                "JOIN SETTLEMENTS s ON b.settlement = s.id " +
                "WHERE s.name = ? ORDER BY b.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, settlementName);
            ResultSet rs = stmt.executeQuery();

            List<String> buildings = new ArrayList<>();
            while (rs.next()) {
                buildings.add(rs.getString("name"));
            }
            return buildings;
        }
    }

    public Integer getBuildingId(String buildingName) throws SQLException {
        String sql = "SELECT id FROM BUILDINGS WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, buildingName);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("id") : null;
        }
    }
}
