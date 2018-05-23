package fr.uha.ensisa.projet2A.monitoring;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Monitoring {

	public static void main(String[] args) throws Exception {
				
		// Get project configuration from config.txt
		MonitoringConfiguration config = new MonitoringConfiguration("E:\\Cours\\2A\\Projet 2A Monitoring\\config.txt");
		System.out.println(config);

		// Initialize ElasticSearch connection, creation of the index "update"
		ElasticSearchUtil.initElasticSearch(config.getClusterNameES(), config.getHostES(), config.getPortES());
		ElasticSearchUtil.indexUpdate();
		
		// Open connection to the DMG SQL Server
		DMG dmg = new DMG();
		dmg.openConnection(config.getHostDMGSQL());

		// Open connection to the machines Moxa
		//Moxa moxa = new Moxa();
		//String[] IPs = { config.getDemeterIP(), config.getHaas1IP(), config.getHaas2IP(), config.getHaas3IP() };

		// Add of a first element into Elasticsearch database 
		if (ElasticSearchUtil.isESDatabaseEmpty()) {
			System.out.println("ES database initialized");
			ElasticSearchUtil.putData(dmg.queryDBHistory().get(0));
		}
		
		// Each 5 secondes, if the last modified date has changed then we load the new data
		Runnable monitoringRunnable = new Runnable() {
			public void run() {
				try {
					// Init
					String lastESDate = ElasticSearchUtil.getLastUpdateTime();
					String lastSQLDate = dmg.getLastUpdateTime();
					int i = 0;
					
					System.out.println("lastESDate = " + lastESDate);
					System.out.println("lastSQLDate = " + lastSQLDate);
					
					// DMG
					if (!lastESDate.equals(lastSQLDate)) {
						System.out.println("New data from DMG_CTX SQL Server");
						System.out.println("****** Loading new data ****** ");
						for (MachineUpdate update : dmg.getUpdatesFromLastDate(lastESDate)) {
							ElasticSearchUtil.putData(update);
							System.out.println(update);
							i++;
						}
					}

					// Moxa
					/*ArrayList<MachineUpdate> updates = moxa.readTransaction(IPs, config.getMoxaPort());
					if (!updates.isEmpty()) {
						System.out.println("New data from machines connected with a Moxa");
						System.out.println("****** Loading new data ****** ");
						for (MachineUpdate update : updates) {
							ElasticSearchUtil.putData(update);
							System.out.println(update);
							i++;
						}
					}*/
					System.out.println("******" + i + " file(s) charged into ElasticSearch database ******");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};

		ScheduledExecutorService monitoringExecutor = Executors.newScheduledThreadPool(1);
		monitoringExecutor.scheduleAtFixedRate(monitoringRunnable, 0, 5, TimeUnit.SECONDS);

	}

}
