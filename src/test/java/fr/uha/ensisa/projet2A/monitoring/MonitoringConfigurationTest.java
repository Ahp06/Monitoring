package fr.uha.ensisa.projet2A.monitoring;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class MonitoringConfigurationTest {

	private MonitoringConfiguration sut;

	@Before
	public void create() {
		String[] IPs = { "demeterIP", "haas1IP", "haas2IP", "haas3IP", };
		String[] machineNames = { "Demeter", "HAAS_VF2_5AXES", "HAAS_VF2_3AXES", "HAAS_SL20" };
		sut = new MonitoringConfiguration("clusterName", "localhost", 9200, "bdd", IPs, machineNames, 8080, 1, 5);
	}

	@Test
	public void setClusterName() {
		assertEquals(sut.getClusterNameES(), "clusterName");
		sut.setClusterNameES("new cluster name");
		assertEquals(sut.getClusterNameES(), "new cluster name");
	}

	@Test
	public void setHostES() {
		assertEquals(sut.getHostES(), "localhost");
		sut.setHostES("new host");
		assertEquals(sut.getHostES(), "new host");
	}

	@Test
	public void setPortES() {
		assertEquals(sut.getPortES(), 9200);
		sut.setPortES(8080);
		assertEquals(sut.getPortES(), 8080);
	}

	@Test
	public void setSQLHost() {
		assertEquals(sut.getHostDMGSQL(), "bdd");
		sut.setHostDMGSQL("new bdd");
		assertEquals(sut.getHostDMGSQL(), "new bdd");
	}

	@Test
	public void setIPs() {
		String[] IPs = { "demeterIP", "haas1IP", "haas2IP", "haas3IP", };
		String[] newIPs = { "IP1", "IP2" };
		for (int i = 0; i < sut.getIPs().length; i++) {
			assertEquals(sut.getIPs()[i], IPs[i]);
		}
		sut.setIPs(newIPs);
		for (int i = 0; i < sut.getIPs().length; i++) {
			assertEquals(sut.getIPs()[i], newIPs[i]);
		}
	}

	@Test
	public void setMoxaPort() {
		assertEquals(sut.getMoxaPort(), 8080);
		sut.setMoxaPort(3636);
		assertEquals(sut.getMoxaPort(), 3636);
	}

	@Test
	public void show() {
		String printed = "**** Monitoring configuration : ****\n" + "Elasticsearch cluster name = clusterName\n"
				+ "Elasticsearch host = localhost\n" + "Elasticsearch port = 9200\n" + "DMG SQL server host  = bdd\n"
				+ "Machine : Demeter , IP = demeterIP\n" + "Machine : HAAS_VF2_5AXES , IP = haas1IP\n"
				+ "Machine : HAAS_VF2_3AXES , IP = haas2IP\n" + "Machine : HAAS_SL20 , IP = haas3IP\n"
				+ "Moxa port = 8080\n" +"Moxa pooling period = 1\n" +"DMG pooling period = 5\n" + "**** End of configuration ****";

		assertEquals(printed, sut.toString());
	}

	@Test
	public void configByfile() throws FileNotFoundException, URISyntaxException {
		
		URL url = this.getClass().getClassLoader().getResource("configTest.txt"); 
		
		// Without this line the url is "D:/Cours/2A/Projet%202A%20Monitoring/Monitoring/target/test-classes/configTest.txt"
		// and because of "Project%202A%20" characters this test was throwing a FileNotFoundException 
		File file = FileUtils.toFile(url); 

		sut = new MonitoringConfiguration(file.getAbsolutePath());
		String machineNames[] = { "machine1", "machine2" };
		String IPs[] = { "IP1", "IP2" };

		assertEquals(sut.getClusterNameES(), "elasticsearch");
		assertEquals(sut.getHostES(), "localhost");
		assertEquals(sut.getPortES(), 9300);
		assertEquals(sut.getHostDMGSQL(), "bdd");
		for (int i = 0; i < machineNames.length; i++) {
			assertEquals(sut.getMachineNames()[i], "machine" + (i + 1));
			assertEquals(sut.getIPs()[i], IPs[i]);
		}
		assertEquals(sut.getMoxaPort(), 8080);
		assertEquals(sut.getMoxaPoolingPeriod(), 1);
		assertEquals(sut.getDmgPoolingPeriod(), 5);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void configByNullFile() throws FileNotFoundException {
		sut = new MonitoringConfiguration(null);
	}

}
