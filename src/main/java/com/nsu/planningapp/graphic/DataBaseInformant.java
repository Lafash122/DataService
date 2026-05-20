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
	public List<BuildingDto> getBuildingsByType(String blueprintType, String settlementName) throws Exception {
		return queryService.getBuildingsByType(blueprintType, settlementName);
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
}