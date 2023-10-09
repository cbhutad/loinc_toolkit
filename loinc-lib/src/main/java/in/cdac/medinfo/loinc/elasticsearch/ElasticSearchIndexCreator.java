package in.cdac.medinfo.loinc.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import in.cdac.medinfo.loinc.exceptions.InternalServerException;

import org.apache.logging.log4j.LogManager;

public class ElasticSearchIndexCreator {

    private static final Logger logger = LogManager.getLogger(ElasticSearchIndexCreator.class);

    public void insert(List<Map<String, Object>> data, String index) throws IOException {

        logger.info("Creating Index for : " + index + " please wait ........");
        List<BulkOperation> bulkoperations = new ArrayList<>();
        int[] counter = {0};
        int size = data.size();
        
        try {
            if(ElasticSearchConfiguration.isIndexExists(index)) {
                List<String> indexList = new ArrayList<>();
                indexList.add(index);
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(indexList).build();
                ElasticSearchConfiguration.elasticsearchClient.indices().delete(deleteIndexRequest);
            }
            for(Map<String, Object> jsonMap : data) {
                bulkoperations.add(IndexOperation.of(c -> c.document(jsonMap).id(Integer.toString(counter[0]++)))._toBulkOperation());
                if(counter[0] % 100 == 0 || counter[0] == size) {
                    BulkResponse bulkresponse = ElasticSearchConfiguration.elasticsearchClient.bulk(r -> r.index(index).operations(bulkoperations));
                    if(bulkresponse.errors()) {
                        System.out.println("Error while executing bulk operations");
                    }
                    bulkoperations.clear();
                }
                if(counter[0] % 1000 == 0) {
                    logger.info(counter[0] + " documents indexing completed");
                }
            }
            logger.info("Indexing for : " + index + " completed");
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage() + " connection not established with elasticsearch server");
            throw new InternalServerException("ERROR : " + ex.getMessage() + " connection not established with elasticsearch server");
        }
    }
    
}
