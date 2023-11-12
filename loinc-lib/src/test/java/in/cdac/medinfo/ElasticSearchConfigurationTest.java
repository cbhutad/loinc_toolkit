package in.cdac.medinfo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import in.cdac.medinfo.loinc.PropertyReader;
import in.cdac.medinfo.loinc.elasticsearch.ElasticSearchConfiguration;
import in.cdac.medinfo.loinc.exceptions.InternalServerException;

public class ElasticSearchConfigurationTest {

    private static ElasticSearchConfiguration elasticSearchConfiguration;

    @BeforeAll
    static void setup() {
        PropertyReader.elasticsearchHost = "http://localhost";
        PropertyReader.elasticsearchHost = "9200";
        elasticSearchConfiguration = new ElasticSearchConfiguration();
    }

    @Test
    void makeConnectionTest() {
        ElasticSearchConfiguration.elasticsearchClient = elasticSearchConfiguration.makeConnection();
        assertNotNull(ElasticSearchConfiguration.elasticsearchClient);
    }

    @Test
    void closeConnectionTest() {
        elasticSearchConfiguration.closeConnection();
        assertNull(ElasticSearchConfiguration.elasticsearchClient);
    }

    //Before indexes are created

    @Test
    void startupLoincIndexExistsTest() {
        
        ElasticSearchConfiguration.elasticsearchClient = elasticSearchConfiguration.makeConnection();
        assertNotNull(ElasticSearchConfiguration.elasticsearchClient);
        //Checking if the any of the three indexes exist before creating them
        assertThrows(InternalServerException.class, () -> {ElasticSearchConfiguration.isIndexExists("loinc");});
    }

    @Test
    void startupLoincPanelIndexExistsTest() {
        
        ElasticSearchConfiguration.elasticsearchClient = elasticSearchConfiguration.makeConnection();
        assertNotNull(ElasticSearchConfiguration.elasticsearchClient);
        //Checking if the any of the three indexes exist before creating them
        assertThrows(InternalServerException.class, () -> {ElasticSearchConfiguration.isIndexExists("loincpanel");});
    }

    @Test
    void startupLoincPartIndexExistsTest() {
        
        ElasticSearchConfiguration.elasticsearchClient = elasticSearchConfiguration.makeConnection();
        assertNotNull(ElasticSearchConfiguration.elasticsearchClient);
        //Checking if the any of the three indexes exist before creating them
        assertThrows(InternalServerException.class, () -> {ElasticSearchConfiguration.isIndexExists("loincpart");});
    }

}