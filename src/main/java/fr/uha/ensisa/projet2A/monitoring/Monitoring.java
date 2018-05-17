package fr.uha.ensisa.projet2A.monitoring;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.client.ElasticsearchClient;

public class Monitoring {

	public static void main(String[] args) throws Exception {

		// Get project configuration from config.txt
		MonitoringConfiguration config = new MonitoringConfiguration("E:\\Cours\\2A\\Projet 2A Monitoring\\config.txt");

		// Initialize ElasticSearch connection, creation of the index "update"
		ElasticSearchUtil.initElasticSearch(config.getClusterNameES(), config.getHostES(), config.getPortES());
		ElasticSearchUtil.indexUpdate();


		// Open connection to the DMG SQL Server
		DMG dmg = new DMG();
		dmg.openConnection(config.getHostDMGSQL());
		
		/*System.out.println(dmg.getLastUpdateTime()); //2018-05-17 00:00:00.0
		String last = ElasticSearchUtil.getLastUpdateTime(); //2018-05-14 13:44:09.770
		
		for(MachineUpdate update : dmg.getUpdatesFromLastDate(last)){
			System.out.println(update);
		}*/
			
		//Each secondes, if the last modified date has changed then we load the new data
		Runnable dmgRunnable = new Runnable() {
			public void run() {
				try {
					String lastESDate = ElasticSearchUtil.getLastUpdateTime(); 
					String lastSQLDate = dmg.getLastUpdateTime(); 
					
					if (!lastESDate.equals(lastSQLDate)) {
						System.out.println("New data from DMG SQL Server");
						int i = 0; 
						for (MachineUpdate update : dmg.getUpdatesFromLastDate(lastESDate)) {
							ElasticSearchUtil.putData(update);
							i++;
						}
						System.out.println(i + " file(s) charged into ElasticSearch database");
					}else {
						System.out.println("ElasticSearch database up to date");
					}
				} catch (SQLException | InterruptedException | ExecutionException | ParseException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(dmgRunnable, 0, 3, TimeUnit.SECONDS);

	}

}
