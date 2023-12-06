package in.cdac.medinfo.loinc;

/**
 *  This class models the properties entered on the configuration page.
 */
public class ModelProperties {
    
    private String loincFolderPath;
    private String elasticSearchHost;
    private String elasticSearchPort;
    private String logFilePath;

    public String getLoincFolderPath() {
        return this.loincFolderPath;
    }

    public void setLoincFolderPath(String loincFolderPath) {
        this.loincFolderPath = loincFolderPath;
    }

    public String getElasticSearchHost() {
        return this.elasticSearchHost;
    }

    public void setElasticSearchHost(String elasticSearchHost) {
        this.elasticSearchHost = elasticSearchHost;
    }

    public String getElasticSearchPort() {
        return this.elasticSearchPort;
    }

    public void setElasticSearchPort(String elasticSearchPort) {
        this.elasticSearchPort = elasticSearchPort;
    }

    public String getLogFilePath() {
        return this.logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }
}
