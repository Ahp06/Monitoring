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
	
	
}
