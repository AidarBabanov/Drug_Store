package servers.database;

import java.sql.*;

public class DbConnection {

		private Connection con;
		private Statement st;
		private ResultSet rs;

		public DbConnection() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/steppe", "root", "");

			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}
		
		
}
