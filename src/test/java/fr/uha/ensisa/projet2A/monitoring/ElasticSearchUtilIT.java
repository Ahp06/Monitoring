package fr.uha.ensisa.projet2A.monitoring;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Timestamp;

import org.junit.Test;

public class ElasticSearchUtilIT extends ElasticSearchIntegrationTests{

	/*@Test
	public void createIndex() throws IOException {
		
		MachineUpdate test = new MachineUpdate(); 
		test.setMachineID(0);
		test.setMachineName("test");
		test.setState(1);
		test.setStateLabel(ElasticSearchUtil.getStateLabel(test.getState()));
		test.setTime(new Timestamp(System.currentTimeMillis()));
		
		ElasticSearchUtil.indexUpdate(test);
		assertEquals(ElasticSearchUtil.isIndexRegistered(), true);
		
		ElasticSearchUtil.deleteIndex("update");
		assertEquals(ElasticSearchUtil.isIndexRegistered(), false);
	}
	
	@Test
	public void putDataIntoES() throws IOException {
		
		MachineUpdate test = new MachineUpdate(); 
		test.setMachineID(0);
		test.setMachineName("test");
		test.setState(1);
		test.setStateLabel(ElasticSearchUtil.getStateLabel(test.getState()));
		test.setTime(new Timestamp(System.currentTimeMillis()));
		
		ElasticSearchUtil.indexUpdate(test);
		assertEquals(ElasticSearchUtil.isIndexRegistered(), true);
		
		MachineUpdate data = new MachineUpdate(); 
		data.setMachineID(1);
		data.setMachineName("test");
		data.setState(0);
		data.setStateLabel(ElasticSearchUtil.getStateLabel(data.getState()));
		data.setTime(new Timestamp(System.currentTimeMillis()));
		
		assertEquals(ElasticSearchUtil.isESDatabaseEmpty(), true);
		ElasticSearchUtil.putData(data);
		assertEquals(ElasticSearchUtil.isESDatabaseEmpty(), false);
	}*/
}
