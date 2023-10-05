package in.cdac.medinfo.loinc.elasticsearch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.elasticsearch.client.RestClient;
import org.apache.http.HttpHost;
import in.cdac.medinfo.loinc.PropertyReader;
import co.elastic.clients.transport.ElasticsearchTransport;

@Component
public class ElasticSearchConfiguration {
	
	@Autowired
	private PropertyReader objReader;

	private static final Logger logger = LogManager.getLogger(ElasticSearchConfiguration.class);
	private static ElasticsearchClient elasticsearchClient = null;

	public synchronized ElasticsearchClient makeConnection() {
		RestClient restClient = RestClient.builder(new HttpHost(PropertyReader.elasticsearchHost, PropertyReader.elasticsearchPort, "http")).build();
		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
		elasticsearchClient = new ElasticsearchClient(transport);
		logger.info("elasticsearch object created");
		return elasticsearchClient;
	}

	public synchronized void closeConnection() {
		elasticsearchClient = null;
	}
}
