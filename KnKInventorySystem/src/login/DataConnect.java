package login;

import java.sql.Connection;
import java.sql.DriverManager;

import InventorySystem.InventoryConfig;

public class DataConnect {

	public static Connection getConnection() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println(InventoryConfig.getProp().getProperty("dbConnectionString")
					+ InventoryConfig.getProp().getProperty("dbUser")
					+ InventoryConfig.getProp().getProperty("dbPassword"));
			Connection con = DriverManager.getConnection(InventoryConfig.getProp().getProperty("dbConnectionString"),
					InventoryConfig.getProp().getProperty("dbUser"),
					InventoryConfig.getProp().getProperty("dbPassword"));
			return con;
		} catch (Exception ex) {
			System.out.println("Database.getConnection() Error -->" + ex.getMessage());
			return null;
		}
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception ex) {
		}
	}
}