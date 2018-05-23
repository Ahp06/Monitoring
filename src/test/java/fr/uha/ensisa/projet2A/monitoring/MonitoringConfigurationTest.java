package fr.uha.ensisa.projet2A.monitoring;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MonitoringConfigurationTest {
	
	MonitoringConfiguration sut; 
	
	@Before 
	public void create() {
		sut = new MonitoringConfiguration("clusterName","localhost",9200,"bdd","haas1IP","haas2IP","haas3IP","demeterIP",8080); 
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
		assertEquals(sut.getHostDMGSQL(),"new bdd");
	}
	
	@Test
	public void setHaas1IP() {
		assertEquals(sut.getHaas1IP(), "haas1IP");
		sut.setHaas1IP("new haas1IP");
		assertEquals(sut.getHaas1IP(),"new haas1IP");
	}
	
	@Test
	public void setHaas2IP() {
		assertEquals(sut.getHaas2IP(), "haas2IP");
		sut.setHaas2IP("new haas2IP");
		assertEquals(sut.getHaas2IP(),"new haas2IP");
	}
	
	@Test
	public void setHaas3IP() {
		assertEquals(sut.getHaas3IP(), "haas3IP");
		sut.setHaas3IP("new haas3IP");
		assertEquals(sut.getHaas3IP(),"new haas3IP");
	}
	
	@Test
	public void setDemeterIP() {
		assertEquals(sut.getDemeterIP(), "demeterIP");
		sut.setDemeterIP("new demeterIP");
		assertEquals(sut.getDemeterIP(),"new demeterIP");
	}
	
	@Test
	public void setMoxaPort() {
		assertEquals(sut.getMoxaPort(), 8080);
		sut.setMoxaPort(3636);
		assertEquals(sut.getMoxaPort(),3636);
	}
	
	@Test
	public void show() {
		String printed = "**** Monitoring configuration : ****\n" + 
				"Elasticsearch cluster name   = clusterName\n" + 
				"Elasticsearch host = localhost\n" + 
				"Elasticsearch port = 9200\n" + 
				"DMG SQL server host  = bdd\n" + 
				"HAAS_VF2_5AXES IP = haas1IP\n" + 
				"HAAS_VF2_3AXES IP = haas2IP\n" + 
				"HAAS_SL20 IP = haas3IP\n" + 
				"Demeter IP = demeterIP\n" + 
				"Moxa port = 8080\n" + 
				"**** End of configuration ****"; 
		
		assertEquals(printed, sut.toString());
	}
	
	@Test
	public void configByfile() {
		sut  = new MonitoringConfiguration("E:\\Cours\\2A\\Projet 2A Monitoring\\config.txt");
		System.out.println(sut);
		assertEquals(sut.getClusterNameES(), null);
		assertEquals(sut.getHostES(), null);
		assertEquals(sut.getPortES(), 0);
		assertEquals(sut.getHostDMGSQL(), null);
		assertEquals(sut.getHaas1IP(), null);
		assertEquals(sut.getHaas2IP(), null);
		assertEquals(sut.getHaas3IP(), null);
		assertEquals(sut.getDemeterIP(), null);
		assertEquals(sut.getMoxaPort(), 0);
		
	}
	
}
