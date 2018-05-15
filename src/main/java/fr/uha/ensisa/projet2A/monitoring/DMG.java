package fr.uha.ensisa.projet2A.monitoring;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DMG {

	private Connection connection;
	private PreparedStatement st;
	private ResultSet result;
	private Timestamp lastDateModification; 
	private boolean historyCharged; 

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

		if(!this.isHistoryCharged()) {
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
			this.setHistoryCharged(true);
			return updates;
		} 
		
		return null; 
		
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
	 * @return
	 * @throws SQLException
	 */
	public Timestamp queryLastDate() throws SQLException {

		String query = "select Time from mdetail where (select max(Id) from mdetail)=Id";
		this.st = this.connection.prepareStatement(query);
		this.result = st.executeQuery();
		
		Timestamp lastDate = null ; 
		while (this.result.next()) {
			lastDate = result.getTimestamp("Time");
		}
		
		return lastDate; 
	}
	
	public Timestamp getLastDateModification() {
		return lastDateModification;
	}

	public void setLastDateModification(Timestamp lastDateModification) {
		this.lastDateModification = lastDateModification;
	}
	
	public ArrayList<MachineUpdate> getUpdatesFromLastDate(Timestamp lastDate) throws SQLException{
		
		String query = "SELECT Status , Time from mdetail WHERE Time > " + lastDate;
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
		
		return null;
	}

	public boolean isHistoryCharged() {
		return historyCharged;
	}

	public void setHistoryCharged(boolean historyCharged) {
		this.historyCharged = historyCharged;
	}

}
