package fr.uha.ensisa.projet2A.monitoring;

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
		
		ElasticSearchUtil.getLastUpdateTime();
			
		//Each secondes, if the last modified date has changed then we load the new data
		/*Runnable helloRunnable = new Runnable() {
			public void run() {
				try {
					Timestamp lastDate = dmg.queryLastDate();
					if (!lastDate.equals(dmg.getLastDateModification())) {
						System.out.println("New data from DMG SQL Server");
						dmg.setLastDateModification(lastDate);
						for (MachineUpdate update : dmg.getUpdatesFromLastDate(lastDate)) {
							ElasticSearchUtil.putData(update);
						}
					}
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);*/

	}

}
