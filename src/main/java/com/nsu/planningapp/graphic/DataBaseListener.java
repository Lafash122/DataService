package com.nsu.planningapp.graphic;

import java.sql.Connection;

public interface DataBaseListener {
	public String getDBName();
	public Connection getConnection() throws Exception;
	public boolean areTablesExist(Connection connection) throws Exception;
	public void createAndFillDatabase(Connection connection) throws Exception;
	public void createNewDatabase(Connection connection) throws Exception;
}