package fr.uha.ensisa.projet2A.monitoring;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.dbunit.DBTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;

public class DMGTest extends DBTestCase {

	private DMG dmg;
	private IDatabaseTester databaseTester;
	private ResultSet result;
	private PreparedStatement statement;
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		URL url = this.getClass().getClassLoader().getResource("sample.xml"); 
		File file = FileUtils.toFile(url); 
		
		return new FlatXmlDataSetBuilder().build(new FileInputStream(file.getAbsolutePath()));
	}
	
	@Override
	protected void setUp() throws Exception {
		databaseTester = new JdbcDatabaseTester("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file", "sa", "");
		databaseTester.setDataSet(this.getDataSet());
		
		this.statement = databaseTester.getConnection().getConnection().prepareStatement("SELECT * FROM * "); 
		this.result = this.statement.executeQuery(); 
		
		while(result.next()){
			System.out.println("oidsghdj");
		}
		
		/*dmg = new DMG();
		dmg.openConnection(data); */
	}
	
	@Override
	protected void tearDown() throws Exception {
		databaseTester.onTearDown();
	}
	
	@Test
	public void testQueryHistory() throws SQLException{
		dmg.queryDBHistory(); 
		assertEquals(true, true);
	}
	
}
