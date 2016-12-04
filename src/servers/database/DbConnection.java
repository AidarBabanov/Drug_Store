package servers.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DbConnection {

	private Connection con;
	private Statement st;
	private ResultSet rs;

	public DbConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/drug_store", "root", "");
			st = con.createStatement();
		} catch (Exception e) {
			System.out.println("DbConnection: " + e.getMessage());
		}
	}

	public void SignUpClient(String login, String password, String name) throws Exception {
		st.executeUpdate("insert into client values ('" + login + "', '" + password + "', '" + name + "');");
	}

	public void SignUpCreator(String login, String password, String name, String specialization) throws Exception {
		st.executeUpdate("insert into creator values ('" + login + "', '" + password + "', '" + name + "', "
				+ specialization + ");");
	}

	public boolean SignIn(String cl_cr, String login, String password) throws SQLException {
		rs = st.executeQuery(
				"Select * from " + cl_cr + " where login='" + login + "' and password='" + password + "';");
		if (!rs.next())
			return false;
		return true;
	}

	public List<JsonObject> getListOfDrugs() throws SQLException {
		List<JsonObject> result = new ArrayList<JsonObject>();
		rs = st.executeQuery("SELECT * FROM drug_store.drug_type;");
		while (rs.next()) {
			JsonObject OneDrugType = new JsonObject();
			OneDrugType.addProperty("id_drugtype", rs.getString("id_drugtype"));
			OneDrugType.addProperty("drug_name", rs.getString("drug_name"));
			OneDrugType.addProperty("description", rs.getString("description"));
			OneDrugType.addProperty("price_per_kilo", rs.getString("price_per_kilo"));
			result.add(OneDrugType);
		}
		return result;
	}

	public List<String> getDrugType(String id_drugtype) throws SQLException {
		List<String> result = new ArrayList<String>();
		rs = st.executeQuery("SELECT * FROM drug_store.drug_type where id_drugtype=" + id_drugtype + ";");
		rs.next();
		result.add(rs.getString("id_drugtype"));
		result.add(rs.getString("drug_name"));
		result.add(rs.getString("description"));
		result.add(rs.getString("price_per_kilo"));
		return result;
	}

	public void addBatch(String weight, String client_login, String id_drug_type, String payment_type, String payment,
			String destination) throws Exception {
		st.executeUpdate(
				"Insert into batch  (weight, client_login, id_drug_type, payment_type, payment, destination) values ("
						+ weight + ", '" + client_login + "', " + id_drug_type + ", '" + payment_type + "', " + payment
						+ ", '" + destination + "');");
	}

	public List<JsonObject> getListofBatchesAccept(String id_drug_type) throws SQLException {
		List<JsonObject> result = new ArrayList<JsonObject>();
		rs = st.executeQuery("select * from batch where creator_login is NULL and id_drug_type=" + id_drug_type
				+ " and completed=0;");
		while (rs.next()) {
			JsonObject OneBatch = new JsonObject();
			OneBatch.addProperty("id_batch", rs.getString("id_batch"));
			OneBatch.addProperty("weight", rs.getString("weight"));
			OneBatch.addProperty("client_login", rs.getString("client_login"));
			OneBatch.addProperty("payment_type", rs.getString("payment_type"));
			OneBatch.addProperty("payment", rs.getString("payment"));
			OneBatch.addProperty("destination", rs.getString("destination"));
			result.add(OneBatch);
		}
		return result;
	}

	public List<String> getCreator(String login) throws SQLException {
		List<String> result = new ArrayList<String>();
		rs = st.executeQuery("SELECT * FROM drug_store.creator where login='" + login + "';");
		rs.next();
		result.add(rs.getString("login"));
		result.add(rs.getString("password"));
		result.add(rs.getString("name"));
		result.add(rs.getString("login"));
		result.add(rs.getString("specialization"));
		return result;
	}

}