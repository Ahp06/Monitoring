package fr.uha.ensisa.projet2A.monitoring;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import org.junit.Test;

public class ElasticSearchUtilIT extends ElasticSearchIntegrationTests {

	@Test
	public void elasticsearchIT() throws IOException, InterruptedException, ExecutionException, ParseException {

		// First object for indexation
		Timestamp firstTime = new Timestamp(5000);
		MachineUpdate test = new MachineUpdate();
		test.setMachineID(0);
		test.setMachineName("test");
		test.setState(1);
		test.setStateLabel(ElasticSearchUtil.getStateLabel(test.getState()));
		test.setTime(firstTime);

		ElasticSearchUtil.indexUpdate(test);
		assertEquals(ElasticSearchUtil.isIndexRegistered(), true);
		ElasticSearchInMemory.refresh();

		// First document
		Timestamp current = new Timestamp(50000);
		MachineUpdate data1 = new MachineUpdate();
		data1.setMachineID(1);
		data1.setMachineName("test IT");
		data1.setState(0);
		data1.setStateLabel(ElasticSearchUtil.getStateLabel(data1.getState()));
		data1.setTime(current);

		ElasticSearchUtil.putData(data1);
		ElasticSearchInMemory.refresh();
		assertEquals(ElasticSearchUtil.isESDatabaseEmpty(), false);

		// Retrieving the date of the last update
		assertEquals(ElasticSearchUtil.getLastUpdateTime(), current.toString());

		// Delete index
		ElasticSearchUtil.deleteIndex("update");

	}

	@Test
	public void getStateLabel() {
		assertEquals(ElasticSearchUtil.getStateLabel(0), "Off");
		assertEquals(ElasticSearchUtil.getStateLabel(1), "Stop");
		assertEquals(ElasticSearchUtil.getStateLabel(2), "Run");
		assertEquals(ElasticSearchUtil.getStateLabel(3), "Alarm");
	}

	@Test
	public void getStateByLabel() {
		assertEquals(ElasticSearchUtil.getStateByLabel("Off"), 0);
		assertEquals(ElasticSearchUtil.getStateByLabel("Stop"), 1);
		assertEquals(ElasticSearchUtil.getStateByLabel("Run"), 2);
		assertEquals(ElasticSearchUtil.getStateByLabel("Alarm"), 3);
	}

}
