package fr.uha.ensisa.projet2A.monitoring;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class ElasticSearchIntegrationTests {
	
	@BeforeClass
	public static void createESInMemory() throws Exception{
		ElasticSearchInMemory.setUp();
		ElasticSearchUtil.initElasticSearch("elasticsearch_integration", "127.0.0.1", 9300);
	}
	
	@AfterClass
	public static void closeESInMemory() throws Exception{
		ElasticSearchInMemory.tearDown();
	}

}
