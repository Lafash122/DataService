package com.nsu.planningapp.graphic;

import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseInitializer;
import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;
import com.nsu.planningapp.planningapp.service.QueryService;
import com.nsu.planningapp.planningapp.service.PreliminaryQueryService;
import com.nsu.planningapp.planningapp.dto.*;

import java.sql.Connection;
import java.util.List;

public class DataBaseInformant implements DataBaseListener {
	private final QueryService queryService = new QueryService();
	private final PreliminaryQueryService preliminaryService = new PreliminaryQueryService();

	@Override
	public String getDBName() {
		return DatabaseConnection.getDBName();
	}

	@Override
	public Connection getConnection() throws Exception {
		return DatabaseConnection.getConnection();
	}

	@Override
	public boolean areTablesExist(Connection connection) throws Exception {
		return DatabaseInitializer.areTablesPresent(connection);
	}

	@Override
	public void createAndFillDatabase(Connection connection) throws Exception {
		DatabaseInitializer.createTables(connection);
		DatabaseInitializer.createFromDefaultFiles(connection);
	}

	@Override
	public void createNewDatabase(Connection connection) throws Exception {
		DatabaseInitializer.dropTables(connection);
		createAndFillDatabase(connection);
	}


	// 1
	@Override
	public int getTotalResidentCapacity(Integer settlementId) throws Exception {
		return queryService.getTotalResidentCapacity(settlementId);
	}

	// 2
	@Override
	public List<BuildingInfoDto> getBuildingsByType(String blueprintType, String settlementName) throws Exception {
		return queryService.getBuildingsByType(blueprintType, settlementName);
	}

	// 3
	@Override
	public int getMaxResourceProduction(Integer resourceId, Integer settlementId) throws Exception {
		return queryService.getMaxResourceProduction(resourceId, settlementId);
	}

	// 4
	@Override
	public int getMaxResourceConsumption(Integer resourceId, Integer cityId) throws Exception {
		return queryService.getMaxResourceConsumption(resourceId, cityId);
	}

	// 5
	//@Override
	

	// 6
	@Override
	public double getTotalResourceStorage(Integer resourceId, Integer settlementId) throws Exception {
		return queryService.getTotalResourceStorage(resourceId, settlementId);
	}

	// 7
	//@Override
	

	// 8
	//@Override
	

	// 9
	@Override
	public List<BuildingInfoDto> getBuildingsList(Integer settlementId) throws Exception {
		return queryService.getBuildingsList(settlementId);
	}

	// 10
	//@Override
	public double getDaysToFillStorage(Integer resourceId, Integer settlementId) throws Exception {
		return queryService.getDaysToFillStorage(resourceId, settlementId);
	}

	// 11
	//@Override
	

	// 12
	//@Override
	public double getMaxStorageInNonStorageBuildings(Integer resourceId, Integer settlementId) throws Exception {
		return queryService.getMaxStorageInNonStorageBuildings(resourceId, settlementId);
	}


	@Override
	public List<String> getAllSettlements() throws Exception {
		return PreliminaryQueryService.getAllSettlements();
	}

	@Override
	public Integer getSettlementId(String cityName) throws Exception {
		return PreliminaryQueryService.getSettlementId(cityName);
	}

	@Override
	public List<String> getAllBlueprintTypes() throws Exception {
		return PreliminaryQueryService.getAllBlueprintTypes();
	}

	@Override
	public List<String> getAllResourcesNames() throws Exception {
		return PreliminaryQueryService.getAllResourcesNames();
	}

	@Override
	public Integer getResourceId(String resourceName) throws Exception {
		return PreliminaryQueryService.getResourceId(resourceName);
	}
}