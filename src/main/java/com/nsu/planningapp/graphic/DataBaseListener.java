package com.nsu.planningapp.graphic;

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

	public List<String> getAllSettlements() throws Exception;
	public Integer getSettlementId(String cityName) throws Exception;
}