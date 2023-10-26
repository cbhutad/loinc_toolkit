package in.cdac.medinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import in.cdac.medinfo.loinc.CSVReader;
import in.cdac.medinfo.loinc.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CSVReaderTest {

    private static File loincFile ;
    private static File panelAndFormFile;
    private static File partFile;
    private static CSVReader csvReader;

    @BeforeAll
    static void setup() {
        String systemSeparator = System.getProperty("file.separator");
        loincFile = new File("/home/cbhutad/Work/Loinc_Toolkit_src/LOINC-study/LoincCSV" + systemSeparator + Constants.LOINC);
        panelAndFormFile = new File("/home/cbhutad/Work/Loinc_Toolkit_src/LOINC-study/LoincCSV" + systemSeparator + Constants.PANEL_AND_FORMS);
        partFile = new File("/home/cbhutad/Work/Loinc_Toolkit_src/LOINC-study/LoincCSV" + systemSeparator + Constants.PART);
        csvReader = new CSVReader();
    }

    @Test
    void readingFromLoincCSVTest() throws IOException {
        List<Map<String, Object>> resultList = csvReader.readObjectsFromCsv(loincFile);
        assertNotNull(resultList);
        assertEquals(99687, resultList.size());
    }
    
}
