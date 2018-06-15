package fr.uha.ensisa.projet2A.monitoring;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

public class DMGTest {

	private static DMG dmg;
	private static ResultSet result;
	private static PreparedStatement statement;
	private static Statement stmt;

	private Timestamp timestamp = java.sql.Timestamp.valueOf("2017-05-23 15:57:14.853");
	private Timestamp timestamp2 = java.sql.Timestamp.valueOf("2017-05-24 16:00:15.853");

	@BeforeClass
	public static void setUp() throws Throwable {
		Connection con = null;
		dmg = new DMG();
		int re = 0;
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			
			dmg.openConnection("jdbc:hsqldb:mem:memdb;shutdown=true");
			stmt = dmg.getConnection().createStatement();

			re = stmt.executeUpdate(
					"CREATE TABLE mdetail (Id INT , Status VARCHAR(255) NOT NULL, Time VARCHAR(255) NOT NULL); ");
			stmt.executeUpdate(
					"INSERT Into mdetail (Id, Status, Time) VALUES " + "('1','Alarm','2017-05-23 15:57:14.853')");
			stmt.executeUpdate(
					"INSERT Into mdetail (Id, Status, Time) VALUES " + "('2','Stop','2017-05-24 16:00:15.853')");

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		System.out.println("Table created successfully");
		System.out.println("Insert Done");

	}

	public ArrayList<MachineUpdate> listcreation() throws SQLException {
		ArrayList<MachineUpdate> test_updates = new ArrayList<>();
		MachineUpdate update = new MachineUpdate();

		update.setMachineID(1);
		update.setMachineName("DMG_CTX");
		update.setState(3);
		update.setStateLabel("Alarm");
		update.setTime(timestamp);

		MachineUpdate update2 = new MachineUpdate();
		update2.setMachineID(1);
		update2.setMachineName("DMG_CTX");
		update2.setState(1);
		update2.setStateLabel("Stop");
		update2.setTime(timestamp2);

		test_updates.add(update);
		test_updates.add(update2);

		return test_updates;
	}

	public ArrayList<MachineUpdate> getUpdateFromLastDate() throws SQLException {

		ArrayList<MachineUpdate> test_updates = new ArrayList<>();
		MachineUpdate update = new MachineUpdate();

		update.setMachineID(1);
		update.setMachineName("DMG_CTX");
		update.setState(1);
		update.setStateLabel("Stop");
		update.setTime(timestamp2);
		test_updates.add(update);
		return test_updates;
	}

	@Test
	public void testConnection() throws Exception {
		assertEquals(dmg.getConnection().getMetaData().getURL(), "jdbc:hsqldb:mem:memdb;shutdown=true");
	}

	@Test(expected = java.sql.SQLException.class)
	public void testConnectionFailed() throws SQLException{
		dmg.openConnection(null);
	}

	@Test
	public void testQueryHistory() throws Throwable {
		System.out.println(dmg.queryDBHistory());
		System.out.println(this.listcreation());
		assertEquals(dmg.queryDBHistory().toString(), this.listcreation().toString());
	}

	/*@Test
	public void testGetLastUpdate() throws Exception {
		Timestamp timestamp2 = java.sql.Timestamp.valueOf("2017-05-24 18:00:15.853");
		assertEquals(dmg.getLastUpdateTime().toString(), timestamp2.toString());

	}

	@Test
	public void testgetUpdatesFromLastDate() throws SQLException {
		System.out.println(dmg.getUpdatesFromLastDate("2017-05-23 15:57:14.853"));
		System.out.println(this.getUpdateFromLastDate().toString());
		assertEquals(dmg.getUpdatesFromLastDate("2017-05-23 15:57:14.853").toString(),
				"[MachineUpdate [machineID=1, machineName=DMG_CTX, state=1, stateLabel=Stop, time=2017-05-24 18:00:15.853]]");
	}*/

}
