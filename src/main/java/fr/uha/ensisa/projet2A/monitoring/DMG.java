package fr.uha.ensisa.projet2A.monitoring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DMG {

	private Connection connection;
	private PreparedStatement st;
	private ResultSet result;

	public DMG() throws ClassNotFoundException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	}

	/**
	 * Open the connection with the DMG SQL Server
	 */
	public void openConnection(String url) {
		try {
			this.connection = DriverManager.getConnection(url);
			System.out.println("Connected to SQL DMG database");
		} catch (SQLException e) {
			System.out.println("Connection to SQL DMG host failed");
			e.printStackTrace();
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
	 * Return the timestamp of the last modification into the database
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getLastUpdateTime() throws SQLException {

		String query = "SELECT Time from mdetail where (select max(Id) from mdetail)=Id";
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
	 * 
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

		return updates;
	}

	public Connection getConnection() {
		return connection;
	}

}
