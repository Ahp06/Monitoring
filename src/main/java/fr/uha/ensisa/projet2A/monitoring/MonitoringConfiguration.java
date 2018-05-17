package fr.uha.ensisa.projet2A.monitoring;

import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;

public class MonitoringConfiguration {

	private String clusterNameES;
	private String hostES;
	private int portES;
	private String hostDMGSQL;
	private String haas1IP; 
	private String haas2IP; 
	private String haas3IP;
	private String demeterIP;
	private int moxaPort; 
	
	/**
	 * Default constructor, fields must be filled
	 * @param clusterNameES
	 * @param hostES
	 * @param portES
	 * @param hostDMGSQL
	 */
	public MonitoringConfiguration(String clusterNameES, String hostES, int portES, String hostDMGSQL,
			String haas1IP, String haas2IP, String haas3IP, String demeterIP, int moxaPort) {
		
		super();
		
		if(clusterNameES.equals("") || hostES.equals("") || hostDMGSQL.equals("") || haas1IP.equals("")
				|| haas2IP.equals("") || haas3IP.equals("") || demeterIP.equals("")) {
			System.out.println("Wrong configuration, all fields must be filled");
			return; 
		}
		
		this.clusterNameES = clusterNameES;
		this.hostES = hostES;
		this.portES = portES;
		this.hostDMGSQL = hostDMGSQL;
		this.haas1IP = haas1IP; 
		this.haas2IP = haas2IP;
		this.haas3IP = haas3IP; 
		this.demeterIP = demeterIP; 
		this.moxaPort = moxaPort; 
		
		System.out.println("Configuration initialized");
	}

	public MonitoringConfiguration(String pathToJsonFile) {
		try (Reader reader = new FileReader(pathToJsonFile)) {

			Gson gson = new Gson();
			MonitoringConfiguration config = gson.fromJson(reader, MonitoringConfiguration.class);

			setClusterNameES(config.getClusterNameES());
			setHostDMGSQL(config.getHostDMGSQL());
			setHostES(config.getHostES());
			setPortES(config.getPortES());
			setHaas1IP(config.getHaas1IP());
			setHaas2IP(config.getHaas2IP());
			setHaas3IP(config.getHaas3IP());
			setDemeterIP(config.getDemeterIP());
			setMoxaPort(config.getMoxaPort());
			
			System.out.println("Configuration initialized");

		} catch (Exception e) {
			System.out.println("Cannot read/parse " + pathToJsonFile + "file");
		}
	}

	public String getClusterNameES() {
		return clusterNameES;
	}

	public void setClusterNameES(String clusterNameES) {
		this.clusterNameES = clusterNameES;
	}

	public String getHostES() {
		return hostES;
	}

	public void setHostES(String hostES) {
		this.hostES = hostES;
	}

	public int getPortES() {
		return portES;
	}

	public void setPortES(int portES) {
		this.portES = portES;
	}

	public String getHostDMGSQL() {
		return hostDMGSQL;
	}

	public void setHostDMGSQL(String hostDMGSQL) {
		this.hostDMGSQL = hostDMGSQL;
	}

	public String getHaas1IP() {
		return haas1IP;
	}

	public void setHaas1IP(String haas1ip) {
		haas1IP = haas1ip;
	}

	public String getHaas2IP() {
		return haas2IP;
	}

	public void setHaas2IP(String haas2ip) {
		haas2IP = haas2ip;
	}

	public String getHaas3IP() {
		return haas3IP;
	}

	public void setHaas3IP(String haas3ip) {
		haas3IP = haas3ip;
	}

	public String getDemeterIP() {
		return demeterIP;
	}

	public void setDemeterIP(String demeterIP) {
		this.demeterIP = demeterIP;
	}

	public int getMoxaPort() {
		return moxaPort;
	}

	public void setMoxaPort(int moxaPort) {
		this.moxaPort = moxaPort;
	}

	@Override
	public String toString() {
		return "MonitoringConfiguration [clusterNameES=" + clusterNameES + ", hostES=" + hostES + ", portES=" + portES
				+ ", hostDMGSQL=" + hostDMGSQL + "]";
	}

}
