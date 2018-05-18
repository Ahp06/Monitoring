package fr.uha.ensisa.projet2A.monitoring;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.client.ElasticsearchClient;

import net.wimpi.modbus.ModbusException;

public class Monitoring {

	public static void main(String[] args) throws Exception {

		// Get project configuration from config.txt
		MonitoringConfiguration config = new MonitoringConfiguration("D:\\Cours\\2A\\Projet 2A Monitoring\\config.txt");
		System.out.println(config);

		// Initialize ElasticSearch connection, creation of the index "update"
		ElasticSearchUtil.initElasticSearch(config.getClusterNameES(), config.getHostES(), config.getPortES());
		ElasticSearchUtil.indexUpdate();

		// Open connection to the DMG SQL Server
		DMG dmg = new DMG();
		dmg.openConnection(config.getHostDMGSQL());

		// Open connection to the machines Moxa
		Moxa moxa = new Moxa();
		
		// Each secondes, if the last modified date has changed then we load the new data
		Runnable dmgRunnable = new Runnable() {
			public void run() {
				try {
					String lastESDate = ElasticSearchUtil.getLastUpdateTime();
					String lastSQLDate = dmg.getLastUpdateTime();
					if (!lastESDate.equals(lastSQLDate)) {
						System.out.println("New data from DMG SQL Server");
						int i = 0;
						System.out.println("****** Loading new data ****** ");
						for (MachineUpdate update : dmg.getUpdatesFromLastDate(lastESDate)) {
							ElasticSearchUtil.putData(update);
							System.out.println(update);
							i++;
						}
						System.out.println("******" + i + " file(s) charged into ElasticSearch database ******");
					}
				} catch (SQLException | InterruptedException | ExecutionException | ParseException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};

		/*
		 * Runnable moxaRunnable = new Runnable() {
		 * 
		 * @Override public void run() { try { //For all IP ...
		 * //moxa.readTransaction(args, 0); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } };
		 */

		//ScheduledExecutorService executorDMG = Executors.newScheduledThreadPool(1);
		// ScheduledExecutorService executorMoxa = Executors.newScheduledThreadPool(1);

		//executorDMG.scheduleAtFixedRate(dmgRunnable, 0, 3, TimeUnit.SECONDS);
		// executorMoxa.scheduleAtFixedRate(moxaRunnable, 0, 3, TimeUnit.SECONDS);

	}

}
