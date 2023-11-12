package in.cdac.medinfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import in.cdac.medinfo.loinc.CSVReader;
import in.cdac.medinfo.loinc.Constants;
import in.cdac.medinfo.loinc.PropertyReader;
import in.cdac.medinfo.loinc.elasticsearch.ElasticSearchConfiguration;
import in.cdac.medinfo.loinc.elasticsearch.ElasticSearchIndexCreator;

public class ElasticSearchIndexCreatorTest {

    private static ElasticSearchIndexCreator elasticSearchIndexCreator = new ElasticSearchIndexCreator();
    private static CSVReader csvReader = new CSVReader();
    private static ElasticSearchConfiguration elasticSearchConfiguration= new ElasticSearchConfiguration();
    private static File loincFile;
    private static File panelAndFormFile;
    private static File partFile;

    @BeforeAll
    static void setup() {
        PropertyReader.elasticsearchHost = "http://localhost";
        PropertyReader.elasticsearchHost = "9200";
        String systemSeparator = System.getProperty("file.separator");
        loincFile = new File("/home/cbhutad/Work/Loinc_Toolkit_src/LOINC-study/LoincCSV" + systemSeparator + Constants.LOINC);
        panelAndFormFile = new File("/home/cbhutad/Work/Loinc_Toolkit_src/LOINC-study/LoincCSV" + systemSeparator + Constants.PANEL_AND_FORMS);
        partFile = new File("/home/cbhutad/Work/Loinc_Toolkit_src/LOINC-study/LoincCSV" + systemSeparator + Constants.PART);
    }

    @Test
    void insertLoincIndex() {
        try {
            List<Map<String, Object>> loincMap = csvReader.readObjectsFromCsv(loincFile);
            ElasticSearchConfiguration.elasticsearchClient = elasticSearchConfiguration.makeConnection();
            elasticSearchIndexCreator.insert(loincMap, "loinc");
            assertTrue(ElasticSearchConfiguration.isIndexExists("loinc"));
        } catch(IOException ex) {
            //DO nothing for now
        }
    }

    @Test
    void insertLoincPanelIndex() {
        try {
            List<Map<String, Object>> loincPanelMap = csvReader.readObjectsFromCsv(panelAndFormFile);
            ElasticSearchConfiguration.elasticsearchClient = elasticSearchConfiguration.makeConnection();
            elasticSearchIndexCreator.insert(loincPanelMap, "loincpanel");
            assertTrue(ElasticSearchConfiguration.isIndexExists("loincpanel"));
        } catch(IOException ex) {
            //DO nothing for now
        }
    }

    @Test
    void insertLoincPartIndex() {
        try {
            List<Map<String, Object>> loincPartMap = csvReader.readObjectsFromCsv(partFile);
            ElasticSearchConfiguration.elasticsearchClient = elasticSearchConfiguration.makeConnection();
            elasticSearchIndexCreator.insert(loincPartMap, "loincpart");
            assertTrue(ElasticSearchConfiguration.isIndexExists("loincpart"));
        } catch(IOException ex) {
            //DO nothing for now
        }
    }

}