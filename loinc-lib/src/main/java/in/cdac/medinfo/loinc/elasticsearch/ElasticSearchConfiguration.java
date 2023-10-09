package in.cdac.medinfo.loinc.elasticsearch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.elasticsearch.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import in.cdac.medinfo.loinc.PropertyReader;
import in.cdac.medinfo.loinc.exceptions.InternalServerException;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;

@Component
public class ElasticSearchConfiguration {

	private static final Logger logger = LogManager.getLogger(ElasticSearchConfiguration.class);
	public static ElasticsearchClient elasticsearchClient = null;

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

	public static boolean isIndexExists(String index) {
		boolean exists = false;
		List<String> indexList = new ArrayList<>();
		indexList.add(index);
		try {
			//Create a exist request to check if index is already existing or not
			ExistsRequest existsRequest = new ExistsRequest.Builder().index(indexList).build();
			//Getting response whether the particular index exist or not.
			BooleanResponse booleanResponse = elasticsearchClient.indices().exists(existsRequest);
			exists = booleanResponse.value();
		} catch (Exception ex) {
			logger.error("ERROR : " + ex.getMessage());
			throw new InternalServerException("ERROR : Conncetion not established with elasticsearch server");
		}
		return exists;
	}
}
