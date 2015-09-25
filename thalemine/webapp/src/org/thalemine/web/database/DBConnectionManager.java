package org.thalemine.web.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.intermine.sql.Database;
import org.intermine.sql.DatabaseFactory;

public final class DBConnectionManager {

	private static final Logger log = Logger.getLogger(DBConnectionManager.class);
	private static Database database;

	private static class DBConnectionManagerHolder {

		public static final DBConnectionManager INSTANCE = new DBConnectionManager();

	}

	public static DBConnectionManager getInstance() {
		initialize();
		return DBConnectionManagerHolder.INSTANCE;
	}

	private static void initialize() {

		Exception exception = null;

		if (database == null) {
			try {
				database = DatabaseFactory.getDatabase(DatabaseService.DATABASE);
			} catch (ClassNotFoundException e) {
				exception = e;
				e.printStackTrace();
			} catch (SQLException e) {
				exception = e;
				e.printStackTrace();
			} finally {

				if (exception != null) {
					log.error("Cannot initialize the database. Database Name: " + DatabaseService.DATABASE
							+ "; Message:" + exception.getMessage() + exception.getCause());
				} else {
					log.info("Database successfully initialized. " + "Database Name: " + DatabaseService.DATABASE + " " + database);
				}

			}
		}

	}

	public static Connection getConnection() throws Exception, SQLException {
		validateState();
		return database.getConnection();
	}

	public static void validateState() throws Exception {

		if (database == null) {
			throw new IllegalStateException("Database must not be null!");
		}

	}

}
