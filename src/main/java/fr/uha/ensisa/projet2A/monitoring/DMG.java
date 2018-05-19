package fr.uha.ensisa.projet2A.monitoring;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;

public class DMG {

	private Connection connection;
	private PreparedStatement st;
	private ResultSet result;

	/**
	 * Open the connection with the DMG SQL Server
	 */
	public void openConnection(String url) {

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			this.connection = DriverManager.getConnection(url);
			System.out.println("Connected to database");
		} catch (Exception e) {
			System.out.println("Connection failed");
			e.printStackTrace();
		}

	}

	/**
	 * Return all columns of mdetail table
	 * 
	 * @throws SQLException
	 */
	public void printmdetailInfo() throws SQLException {

		String query = "SELECT * FROM mdetail";
		this.st = this.connection.prepareStatement(query);
		ResultSet rs = this.st.executeQuery();

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int i = 1; i <= columnCount; i++) {
			String name = rsmd.getColumnName(i);
			System.out.println(name);
		}
	}

	/**
	 * Create a list of MachineUpdate object with the DMG History
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<MachineUpdate> queryDBHistory() throws SQLException {

		String query = "SELECT Status , Time from mdetail";
		this.st = this.connection.prepareStatement(query);
		this.result = st.executeQuery();

		ArrayList<MachineUpdate> updates = new ArrayList<MachineUpdate>();
		while (this.result.next()) {

			MachineUpdate update = new MachineUpdate();
			update.setMachineID(1);
			update.setMachineName("DMG_CTX");
			update.setState(ElasticSearchUtil.getStateByLabel(result.getString("Status")));
			update.setStateLabel(result.getString("Status"));
			update.setTime(result.getTimestamp("Time"));

			updates.add(update);
		}

		return updates;

	}

	/**
	 * Displays all tables of the SQL database
	 * 
	 * @throws SQLException
	 */
	public void printAllTables() throws SQLException {
		DatabaseMetaData md = this.connection.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		while (rs.next()) {
			System.out.println(rs.getString(3)); // Column 3 is the TABLE_NAME
		}
	}

	/**
	 * Return the timestamp of the last modification into the databse
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getLastUpdateTime() throws SQLException {

		String query = "select Time from mdetail where (select max(Id) from mdetail)=Id";
		this.st = this.connection.prepareStatement(query);
		this.result = st.executeQuery();

		Timestamp lastDate = null;
		while (this.result.next()) {
			lastDate = result.getTimestamp("Time");
		}

		return lastDate.toString();
	}
	
	/**
	 * Return the list of updates object from a specified date
	 * @param lastESDate
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<MachineUpdate> getUpdatesFromLastDate(String lastESDate) throws SQLException {
		
		String query = "SELECT Status , Time from mdetail WHERE Time > \'" + lastESDate + "\'"; 
		
		this.st = this.connection.prepareStatement(query);
		this.result = st.executeQuery();
		ArrayList<MachineUpdate> updates = new ArrayList<MachineUpdate>();
		while (this.result.next()) {

			MachineUpdate update = new MachineUpdate();
			update.setMachineID(1);
			update.setMachineName("DMG_CTX");
			update.setState(ElasticSearchUtil.getStateByLabel(result.getString("Status")));
			update.setStateLabel(result.getString("Status"));
			update.setTime(result.getTimestamp("Time"));
			updates.add(update);
		}
		
		Collections.reverse(updates); //The most recent element will be the first into the list
		return updates;
	}

}
