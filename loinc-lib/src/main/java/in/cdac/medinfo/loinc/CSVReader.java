package in.cdac.medinfo.loinc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import in.cdac.medinfo.loinc.exceptions.InternalServerException;

public class CSVReader {
    
    private String err = "ERROR : ";
    private static final Logger logger = LogManager.getLogger(CSVReader.class);

    public List<Map<String,Object>> readObjectsFromCsv(File file) throws IOException {
        
        MappingIterator<Map<String, Object>> csvToMapCreator;
        try {
            CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            csvToMapCreator = csvMapper.reader().forType(Map.class).with(bootstrap).readValues(file);
            logger.info("Reading data from csv file" + file.getName());
        } catch(FileNotFoundException ex) {
            logger.error(ex.getMessage());
            throw new InternalServerException(err + ex.getMessage() + ", please enter correct file path in configuration page");
        } catch(Exception ex) {
            logger.error(err + ex.getMessage());
            throw new InternalServerException(err + ex.getMessage());
        }
        return csvToMapCreator.readAll();
    }
}
