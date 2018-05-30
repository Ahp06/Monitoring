package fr.uha.ensisa.projet2A.monitoring;

import org.junit.Test;

public class ElasticSearchUtilIT {
	
	@Test
	public void initES() {
		ElasticSearchUtil.initElasticSearch("elasticsearch_integration", "localhost", 9400);
	}

}
