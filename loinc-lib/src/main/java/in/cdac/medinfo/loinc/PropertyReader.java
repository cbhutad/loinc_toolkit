package in.cdac.medinfo.loinc;

import in.cdac.medinfo.loinc.exceptions.InternalServerException;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author cheenmayab
 * This class reads properties entered by user in config page.
 * throws InternalServerException
 *
 */

@Component
public class PropertyReader {

	private static final Logger logger = LogManager.getLogger(PropertyReader.class);
	public static String loincFolderPath;
	public static String elasticsearchHost;
	public static int elasticsearchPort;

	public void propertyRead(String loincFolderPath, String elasticsearchHost, String elasticsearchPort) {
		try {
			PropertyReader.loincFolderPath = loincFolderPath;
			PropertyReader.elasticsearchHost = elasticsearchHost;
			PropertyReader.elasticsearchPort = Integer.parseInt(elasticsearchPort);
		} catch (Exception err) {
			logger.error(err.getMessage() + " Please enter correct Configuration details");
			throw new InternalServerException("ERROR :" + err.getMessage() + " Please enter correct Configuration details");
		}

	}

}
