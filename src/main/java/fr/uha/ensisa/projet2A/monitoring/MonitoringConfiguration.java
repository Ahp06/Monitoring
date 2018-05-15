package fr.uha.ensisa.projet2A.monitoring;

import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;

public class MonitoringConfiguration {

	private String clusterNameES;
	private String hostES;
	private int portES;
	private String hostDMGSQL;
	
	/**
	 * Default constructor, fields must be filled
	 * @param clusterNameES
	 * @param hostES
	 * @param portES
	 * @param hostDMGSQL
	 */
	public MonitoringConfiguration(String clusterNameES, String hostES, int portES, String hostDMGSQL) {
		super();
		
		if(clusterNameES.equals("") || hostES.equals("") || hostDMGSQL.equals("")) {
			System.out.println("Wrong configuration, all fields must be filled");
			return; 
		}
		
		this.clusterNameES = clusterNameES;
		this.hostES = hostES;
		this.portES = portES;
		this.hostDMGSQL = hostDMGSQL;
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

	@Override
	public String toString() {
		return "MonitoringConfiguration [clusterNameES=" + clusterNameES + ", hostES=" + hostES + ", portES=" + portES
				+ ", hostDMGSQL=" + hostDMGSQL + "]";
	}

}
