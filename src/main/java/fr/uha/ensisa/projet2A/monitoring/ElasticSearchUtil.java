package fr.uha.ensisa.projet2A.monitoring;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;

public class ElasticSearchUtil {

	private static Client client;

	/**
	 * Initialize ElasticSearch connection
	 * 
	 * @param clusterName
	 * @param host
	 * @param port
	 */
	public static void initElasticSearch(String clusterName, String host, int port) {
		try {
			Settings settings = Settings.builder().put("cluster.name", clusterName).build();
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connection " + clusterName + "@" + host + ":" + port + " established!");
	}

	/**
	 * Close the client stream
	 */
	public static void closeElasticSearch() {
		client.close();
	}

	/**
	 * Return true if the index "update" already exist
	 * 
	 * @param indexName
	 * @return
	 */
	public static boolean isIndexRegistered() {

		IndicesExistsResponse response = client.admin().indices().prepareExists("update")
				.get(TimeValue.timeValueSeconds(1));

		if (!response.isExists()) {
			return false;
		}

		System.out.println("Index already created!");
		return true;
	}

	/**
	 * Returns the label according to the state of the machine
	 * 
	 * @param state
	 * @return
	 */
	public static String getStateLabel(int state) {

		switch (state) {
		case 0:
			return "Off";
		case 1:
			return "Stop";
		case 2:
			return "Run";
		case 3:
			return "Alarm";
		}
		return null;

	}

	public static int getStateByLabel(String label) {

		if (label.equals("Off")) {
			return 0;
		}
		if (label.equals("Stop")) {
			return 1;
		}
		if (label.equals("Run")) {
			return 2;
		}
		if (label.equals("Alarm")) {
			return 3;
		}

		return -1;
	}

	/**
	 * Index an update
	 * 
	 * @throws IOException
	 */
	public static void indexUpdate() throws IOException {

		if (!isIndexRegistered()) {
			IndexResponse response = client.prepareIndex("update", "MachineUpdate")
					.setSource(XContentFactory.jsonBuilder().startObject().field("machineID", 1)
							.field("machineName", "	").field("state", 1).field("stateLabel", getStateLabel(1))
							.field("time", new Date()).endObject())
					.execute().actionGet();
		}
	}

	/**
	 * Put data by parsing an object to JSON into the ElasticSearch index
	 * "update"
	 * 
	 * @param update
	 * @throws IOException
	 */
	public static void putData(MachineUpdate update) throws IOException {

		if (!update.equals(null)) {
			IndexRequest indexRequest = new IndexRequest("update", "MachineUpdate");
			indexRequest.source(XContentFactory.jsonBuilder().startObject().field("machineID", update.getMachineID())
					.field("machineName", update.getMachineName()).field("state", update.getState())
					.field("stateLabel", getStateLabel(update.getState())).field("time", update.getTime()).endObject());

			IndexResponse response = client.index(indexRequest).actionGet();
		}

	}

	/**
	 * Delete a document with a specific index
	 * 
	 * @param id
	 */
	public static void deleteData(int id) {
		DeleteResponse response = client.prepareDelete("update", "updateMachine", Integer.toString(id)).get();
	}

	/**
	 * Delete an index by name
	 * 
	 * @param client
	 * @param indexName
	 */
	public static void deleteIndex(String indexName) {
		try {
			client.admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet();
			System.out.println("Index : " + indexName + " deleted");
		} catch (Exception e) {
			System.out.println("Index : " + indexName + " doesn't exist");
		}

	}

	/**
	 * Return the number of documents for the index update
	 * 
	 * @return
	 */
	public static long getNumberOfDocuments() {
		SearchResponse response = client.prepareSearch("update").setTypes("MachineUpdate")
				.setQuery(QueryBuilders.termQuery("machineID", "1")).setSize(0).get();

		SearchHits hits = response.getHits();
		long hitsCount = hits.getTotalHits();

		return hitsCount;
	}

	/**
	 * Return the field "time" of the last object added into ElastiSearch
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ParseException
	 */
	public static String getLastUpdateTime() throws InterruptedException, ExecutionException, ParseException {

		int position = (int) (getNumberOfDocuments() - 1);
		SearchResponse response = client.prepareSearch("update").setTypes("MachineUpdate")
				.setQuery(QueryBuilders.termQuery("machineID", "1")).setSize(1).setFrom(position).get();

		SearchHits hits = response.getHits();
		String last = hits.getAt(0).getSourceAsMap().get("time").toString();
		
		// We change the date from format yyyy-MM-dd'T'HH:mm:ss.SSSX -> yyyy-MM-dd HH:mm:ss.S
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		Date date = df.parse(last);
		DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		String dateFormatted = outputFormatter.format(date); 

		return dateFormatted; 

	}

}
