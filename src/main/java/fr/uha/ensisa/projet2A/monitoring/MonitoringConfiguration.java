package fr.uha.ensisa.projet2A.monitoring;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;

public class MonitoringConfiguration {

	private String clusterNameES;
	private String hostES;
	private int portES;
	private String hostDMGSQL;
	private String IPs[];
	private String machineNames[];
	private int moxaPort;
	private int poolingPeriod;

	/**
	 * Default constructor, not recommended use
	 */
	public MonitoringConfiguration() {
		this.clusterNameES = "elasticsearch";
		this.hostES = "localhost";
		this.portES = 9300;
		this.hostDMGSQL = "";
		this.IPs = new String[0];
		this.machineNames = new String[0];
		this.moxaPort = 8080;
		this.poolingPeriod = 5;
	}

	/**
	 * Valued constructor, fields must be filled
	 * 
	 * @param clusterNameES
	 * @param hostES
	 * @param portES
	 * @param hostDMGSQL
	 */
	public MonitoringConfiguration(String clusterNameES, String hostES, int portES, String hostDMGSQL, String[] IPs,
			String[] machineNames, int moxaPort, int poolingPeriod) {

		this.clusterNameES = clusterNameES;
		this.hostES = hostES;
		this.portES = portES;
		this.hostDMGSQL = hostDMGSQL;
		this.IPs = IPs;
		this.machineNames = machineNames;
		this.moxaPort = moxaPort;
		this.poolingPeriod = poolingPeriod;

		System.out.println("Configuration initialized");
	}

	/**
	 * Initialize the configuration by reading a .txt file in JSON format
	 * 
	 * @param pathToJsonFile
	 * @throws FileNotFoundException
	 */

	public MonitoringConfiguration(String pathToJsonFile) throws FileNotFoundException {
		
		Reader reader = new FileReader(pathToJsonFile);
		Gson gson = new Gson();
		MonitoringConfiguration config = gson.fromJson(reader, MonitoringConfiguration.class);

		this.setClusterNameES(config.getClusterNameES());
		this.setHostDMGSQL(config.getHostDMGSQL());
		this.setHostES(config.getHostES());
		this.setPortES(config.getPortES());
		this.setIPs(config.getIPs());
		this.setMachineNames(config.getMachineNames());
		this.setMoxaPort(config.getMoxaPort());
		this.setPoolingPeriod(config.getPoolingPeriod());

		System.out.println("Configuration initialized");

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

	public String[] getIPs() {
		return IPs;
	}

	public void setIPs(String[] iPs) {
		IPs = iPs;
	}

	public String[] getMachineNames() {
		return machineNames;
	}

	public void setMachineNames(String[] machineNames) {
		this.machineNames = machineNames;
	}

	public int getMoxaPort() {
		return moxaPort;
	}

	public void setMoxaPort(int moxaPort) {
		this.moxaPort = moxaPort;
	}

	public int getPoolingPeriod() {
		return poolingPeriod;
	}

	public void setPoolingPeriod(int poolingPeriod) {
		this.poolingPeriod = poolingPeriod;
	}

	@Override
	public String toString() {

		StringBuilder tmp = new StringBuilder("**** Monitoring configuration : ****\n");
		tmp.append("Elasticsearch cluster name   = " + this.clusterNameES + "\n");
		tmp.append("Elasticsearch host = " + this.hostES + "\n");
		tmp.append("Elasticsearch port = " + this.portES + "\n");
		tmp.append("DMG SQL server host  = " + this.hostDMGSQL + "\n");
		for (int i = 0; i < machineNames.length; i++) {
			tmp.append("Machine : " + this.machineNames[i] + " , IP = " + this.IPs[i] + "\n");
		}
		tmp.append("Moxa port = " + this.moxaPort + "\n");
		tmp.append("Pooling period = " + this.poolingPeriod + "\n");
		tmp.append("**** End of configuration ****");

		return tmp.toString();
	}

}
