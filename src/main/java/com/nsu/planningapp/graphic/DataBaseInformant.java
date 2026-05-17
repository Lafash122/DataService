package com.nsu.planningapp.graphic;

import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseInitializer;
import com.nsu.planningapp.planningapp.infrastructure.db.DatabaseConnection;

import java.sql.Connection;

public class DataBaseInformant implements DataBaseListener {
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
}