package in.cdac.medinfo;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import in.cdac.medinfo.loinc.PropertyReader;

public class PropertyReaderTest {

    @Test
    void propertyReadTest() {

        String loincFolderPath = "/home/cbhutad/Work/loinc_toolkit/logs/loinc_lib";
        String elasticsearchHost = "http://localhost";
        String port = "9200";
        PropertyReader propertyReader = new PropertyReader();
        propertyReader.propertyRead(loincFolderPath, elasticsearchHost, port);

        assertTrue(PropertyReader.elasticsearchHost.equals(elasticsearchHost));
        assertTrue(PropertyReader.elasticsearchPort == Integer.parseInt(port));
        assertTrue(PropertyReader.loincFolderPath.equals(loincFolderPath));

    }
}
