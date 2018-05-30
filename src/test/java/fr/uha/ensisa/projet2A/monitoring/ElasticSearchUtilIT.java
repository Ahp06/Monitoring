package fr.uha.ensisa.projet2A.monitoring;

import org.junit.Test;

public class ElasticSearchUtilIT {
	
	@Test
	public void initES() {
		ElasticSearchUtil.initElasticSearch("elasticsearch_integration", "127.0.0.1", 9400);
	}
	
	

}
