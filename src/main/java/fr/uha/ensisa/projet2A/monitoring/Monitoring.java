package fr.uha.ensisa.projet2A.monitoring;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Monitoring {

	private static MonitoringConfiguration config;
	private static DMG dmg;
	private static Moxa moxa;
	private static String configFilePath;

	public static void main(String[] args) throws Exception {

		// Get project configuration from your configuration file
		try {
			if (args.length == 0) {
				configFilePath = "D:\\Cours\\2A\\Projet 2A Monitoring\\config.txt";
				config = new MonitoringConfiguration(configFilePath);
				System.out.println(config);
			} else if (args.length == 1) {
				System.out.println("Configuration file charged = " + args[0]);
				config = new MonitoringConfiguration(args[0]);
				System.out.println(config);
			}
		} catch (Exception e) {
			System.out.println("Cannot read/parse txt file : " + configFilePath);
			System.exit(0);
		}

		// Initialization of ES connection
		try {
			ElasticSearchUtil.initElasticSearch(config.getClusterNameES(), config.getHostES(), config.getPortES());
		} catch (Exception e) {
			System.out.println("Have you started Elasticsearch ?");
			e.printStackTrace();
		}

		// Open connection to the DMG SQL Server
		dmg = new DMG();
		try{
			dmg.openConnection(config.getHostDMGSQL());
			System.out.println("Connected to SQL DMG database");
		}catch(Exception e) {
			System.out.println("Connection to SQL DMG host failed");
			e.printStackTrace();
		}

		// Indexation
		ElasticSearchUtil.indexUpdate(dmg.queryDBHistory().get(0));

		// Open connection to the machines Moxa
		moxa = new Moxa();
		final String[] IPs = config.getIPs();
		final String[] machineNames = config.getMachineNames();
		final int moxaPort = config.getMoxaPort();

		// Add of a first element into ES database
		if (ElasticSearchUtil.isESDatabaseEmpty()) {
			ElasticSearchUtil.putData(dmg.queryDBHistory().get(1));
		}

		Runnable dmgRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					// Init
					String lastESDate = ElasticSearchUtil.getLastUpdateTime();
					String lastSQLDate = dmg.getLastUpdateTime();
					int i = 0;

					System.out.println("lastESDate = " + lastESDate);
					System.out.println("lastSQLDate = " + lastSQLDate);

					// DMG
					if (lastESDate != null && !lastESDate.equals(lastSQLDate)) {
						System.out.println("New data from DMG_CTX SQL Server");
						System.out.println("****** Loading new data ****** ");
						for (MachineUpdate update : dmg.getUpdatesFromLastDate(lastESDate)) {
							ElasticSearchUtil.putData(update);
							System.out.println(update);
							i++;
						}
					}

					if (i == 0) {
						System.out.println("****** No data from the DMG CTX SQL server ******");
					} else {
						System.out.println("******" + i + " update(s) charged from the DMG CTX SQL server  ******");
					}

				} catch (InterruptedException | ExecutionException | ParseException | SQLException | IOException e) {
					try {
						if (dmg.getConnection().isClosed()) {
							System.out.println("Connection lost with SQL Server. Reboot...");
							dmg.openConnection(config.getHostDMGSQL());
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		};

		Runnable moxaRunnable = new Runnable() {

			@Override
			public void run() {
				try {
					int i = 0;
					ArrayList<MachineUpdate> updates = moxa.pooling(IPs, machineNames, moxaPort);
					if (!updates.isEmpty()) {
						System.out.println("New data from machines connected with a Moxa");
						System.out.println("****** Loading new data ****** ");
						for (MachineUpdate update : updates) {
							ElasticSearchUtil.putData(update);
							System.out.println(update);
							i++;
						}
					}
					if (i == 0) {
						System.out.println("****** No data from machines connected with a moxa ******");
					} else {
						System.out.println("******" + i + " update(s) charged from machines connected with a moxa ******");
					}

				} catch (Exception e) {

				}
			}
		};

		//Pooling 1 sec ? 
		ScheduledExecutorService monitoringExecutor = Executors.newScheduledThreadPool(2);
		monitoringExecutor.scheduleAtFixedRate(dmgRunnable, 0, config.getPoolingPeriod(), TimeUnit.SECONDS);
		monitoringExecutor.scheduleAtFixedRate(moxaRunnable, 0, config.getPoolingPeriod(), TimeUnit.SECONDS);

	}
}
