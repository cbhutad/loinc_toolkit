package in.cdac.medinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import in.cdac.medinfo.loinc.PropertyReader;
import in.cdac.medinfo.loinc.exceptions.InternalServerException;

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

    @Test
    void propertyReadFileLogTest() {
        String loincFolderPath = "/home/cbhutad/Work/loinc_toolkit/logs/loinc_lib";
        String elasticsearchHost = "http://localhost";
        String port = "Nine thousand two hundread";

        PropertyReader propertyReader = new PropertyReader();
        Exception ex = assertThrows(InternalServerException.class, () -> propertyReader.propertyRead(loincFolderPath, elasticsearchHost, port));
        assertEquals("ERROR :For input string: \"Nine thousand two hundread\" Please enter correct Configuration details", ex.getMessage());
    }
}
