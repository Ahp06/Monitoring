package fr.uha.ensisa.projet2A.monitoring;

import java.util.Collections;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.transport.Netty4Plugin;

public class ElasticSearchInMemory {

	private static Client client = null;
	private static Node elasticSearchNode = null;

	// Use of a transport plugin because "transport.type = local" isn't authorized
	// anymore on ES version > 5.x
	static class PluginNode extends Node {
		public PluginNode(Settings settings) {
			super(InternalSettingsPreparer.prepareEnvironment(settings, null),
					Collections.singletonList(Netty4Plugin.class));
		}
	}

	public static Client getClient() {
		return client;
	}

	public static void setUp() throws Exception {

		Settings settings = Settings.builder()
				.put("cluster.name", "elasticsearch_integration")
				.put("bootstrap.memory_lock",true)
				.put("path.home", "target")
				.put("transport.type", "netty4")
				.put("http.enabled", false)
				.put("http.type", "netty4")
				.build();

		Node elasticSearchNode = new PluginNode(settings);

		elasticSearchNode.start();
		client = elasticSearchNode.client();
	}

	public static void tearDown() throws Exception {
		if (client != null) {
			client.close();
		}
		if (elasticSearchNode != null) {
			elasticSearchNode.close();
		}

	}
	
	public static void refresh() {
		getClient().admin().indices().prepareRefresh("update").execute().actionGet(); 
	}

}
