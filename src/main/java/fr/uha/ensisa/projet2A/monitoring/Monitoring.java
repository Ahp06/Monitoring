package fr.uha.ensisa.projet2A.monitoring;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Monitoring {

	public static void main(String[] args) throws Exception {

		// Get project configuration from config.txt
		MonitoringConfiguration config = new MonitoringConfiguration("D:\\Cours\\2A\\Projet 2A Monitoring\\config.txt");

		// Initialize ElasticSearch connection, creation of the index "update"
		ElasticSearchUtil.initElasticSearch(config.getClusterNameES(), config.getHostES(), config.getPortES());
		ElasticSearchUtil.indexUpdate();


		// Open connection to the DMG SQL Server
		DMG dmg = new DMG();
		dmg.openConnection(config.getHostDMGSQL());
			
		//Each secondes, if the last modified date has changed then we load the new data
		Runnable helloRunnable = new Runnable() {
			public void run() {
				try {
					String lastESDate = ElasticSearchUtil.getLastUpdateTime(); 
					String lastSQLDate = dmg.getLastUpdateTime(); 
					
					if (!lastESDate.equals(lastSQLDate)) {
						System.out.println("New data from DMG SQL Server");
						for (MachineUpdate update : dmg.getUpdatesFromLastDate(lastESDate)) {
							ElasticSearchUtil.putData(update);
						}
					}
				} catch (SQLException | IOException | InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);

	}

}
