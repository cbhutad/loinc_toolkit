package in.cdac.medinfo.loinc;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import in.cdac.medinfo.loinc.elasticsearch.ElasticSearchIndexCreator;

@Component
public class LoincIndexImplementor {

    ElasticSearchIndexCreator elasticSearchIndexCreator = null;
    CSVReader csvReader = null;
    String separator = System.getProperty("file.separator");

    public void loincIndexingService() throws IOException {
        elasticSearchIndexCreator = new ElasticSearchIndexCreator();
        csvReader = new CSVReader();
        File file = new File(PropertyReader.loincFolderPath + separator + Constants.LOINC);
        List<Map<String, Object>> data = csvReader.readObjectsFromCsv(file); 
        elasticSearchIndexCreator.insert(data, "loinc");
    }

    public void loincPanelIndexingService() throws IOException {
        if(elasticSearchIndexCreator == null) {
            elasticSearchIndexCreator = new ElasticSearchIndexCreator();
        }
        if(csvReader == null) {
            csvReader = new CSVReader();
        }
        File file = new File(PropertyReader.loincFolderPath + separator + Constants.PANEL_AND_FORMS);
        List<Map<String, Object>> data = csvReader.readObjectsFromCsv(file);
        elasticSearchIndexCreator.insert(data, "loincpanel");
    }

    public void loincPartIndexingService() throws IOException {
        if(elasticSearchIndexCreator == null) {
            elasticSearchIndexCreator = new ElasticSearchIndexCreator();
        }
        if(csvReader == null) {
            csvReader = new CSVReader();
        }
        File file = new File(PropertyReader.loincFolderPath + separator + Constants.PART);
        List<Map<String, Object>> data = csvReader.readObjectsFromCsv(file);
        elasticSearchIndexCreator.insert(data, "loincpart");
    }
    
}
