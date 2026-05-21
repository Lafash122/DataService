package com.nsu.planningapp.graphic;

import com.nsu.planningapp.planningapp.dto.*;

import java.sql.Connection;
import java.util.List;

public interface DataBaseListener {
	public String getDBName();
	public Connection getConnection() throws Exception;
	public boolean areTablesExist(Connection connection) throws Exception;
	public void createAndFillDatabase(Connection connection) throws Exception;
	public void createNewDatabase(Connection connection) throws Exception;

	// 1
	public int getTotalResidentCapacity(Integer settlementId) throws Exception;
	// 2
	public List<BuildingInfoDto> getBuildingsByType(String blueprintType, String settlementName) throws Exception;
	// 3
	public int getMaxResourceProduction(Integer resourceId, Integer settlementId) throws Exception;
	// 4
	public int getMaxResourceConsumption(Integer resourceId, Integer cityId) throws Exception;
	// 5
	
	// 6
	public double getTotalResourceStorage(Integer resourceId, Integer settlementId) throws Exception;
	// 7
	
	// 8
	
	// 9
	public List<BuildingInfoDto> getBuildingsList(Integer settlementId) throws Exception;
	// 10
	public double getDaysToFillStorage(Integer resourceId, Integer settlementId) throws Exception;
	// 11
	
	// 12
	public double getMaxStorageInNonStorageBuildings(Integer resourceId, Integer settlementId) throws Exception;

	public List<String> getAllSettlements() throws Exception;
	public Integer getSettlementId(String cityName) throws Exception;
	public List<String> getAllBlueprintTypes() throws Exception;
	public List<String> getAllResourcesNames() throws Exception;
	public Integer getResourceId(String resourceName) throws Exception;
}