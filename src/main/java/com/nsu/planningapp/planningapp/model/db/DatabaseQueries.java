package com.nsu.planningapp.planningapp.model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseQueries {
    //TODO: возможно, получить сет типов зданий, которые есть в БД (bblueprint)


    public int getTotalResidentCapacity(Integer settlementId) throws SQLException {
        String sql;
        if (settlementId == null) {
            sql = "SELECT COALESCE(SUM(number_of_residents), 0) FROM RESIDENTIAL_BUILDING_BLUEPRINT";
        } else {
            sql = "SELECT COALESCE(SUM(number_of_residents), 0) FROM RESIDENTIAL_BUILDING_BLUEPRINT WHERE id = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (settlementId != null) {
                stmt.setInt(1, settlementId);
            }

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // TODO: запрос 2

    public float getMaxResourceProduction(Integer resourceId, Integer settlementId) throws SQLException {
        String sql;
        if (settlementId == null) {
            sql = "SELECT COALESCE(SUM(quantity), 0) FROM AMOUNT_OF_RESOURCES_PRODUCED WHERE ";
        }
    }
}
