package in.cdac.medinfo.loinc;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import in.cdac.medinfo.loinc.exceptions.InternalServerException;

/**
 * This class passes all the configurations to the loinc lib module and sets the log configuration for the application.
 */
public class Configuration {

    public static Properties property = null;
    public static File file;
    public static Properties logProperty = null;
    public static File logFile;

    @Autowired
    PropertyReader objPropertyReader;

    //This method reads the configuration properties file in resources folder
    public Properties setFlagProperty() throws URISyntaxException, IOException {
        if(property == null) {
            System.out.println("Reading the configuration file ...");
            file = new File(getClass().getResource("/configuration.properties").toURI());
            try (FileReader filereader = new FileReader(file)) {
                property = new Properties();
                property.load(filereader);
            }
            return property;
        }
        return property;
    }

    //This method passes properties to lib module once all propeties are set for the first time
    public void setElasticDetails() {
        objPropertyReader.propertyRead(null, null, null);
    }

    //This method does the log configurations
    public void logFileConfigurations(String logDirPath) {
        try {
            ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
            builder.setStatusLevel(Level.INFO);
            builder.setConfigurationName("RollingBuilder");
            //create console appender
            AppenderComponentBuilder appenderBuilder = builder.newAppender("stdout", "CONSOLE").addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
            appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
            builder.add(appenderBuilder);

            RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.INFO);
            rootLogger.add(builder.newAppenderRef("stdout"));

            //create rolling file appender
            LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout").addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");
            if(logDirPath.contains(".log") || logDirPath.contains(".txt")) {
                appenderBuilder = builder.newAppender("log", "File").addAttribute("fileName", logDirPath).add(layoutBuilder);
                builder.add(appenderBuilder);
            } else {
                appenderBuilder = builder.newAppender("log", "File").addAttribute("fileName", logDirPath + File.separator + "CLNtkLogs.logs").add(layoutBuilder);
                builder.add(appenderBuilder);
            }
            rootLogger.add(builder.newAppenderRef("log"));
            builder.add(rootLogger);
            Configurator.reconfigure(builder.build());
        } catch (Exception ex) {
            throw new InternalServerException("ERROR : " + ex.getMessage() + " Please enter correct log directory details");
        }
    }

    //this methods passes all the config detais to the loinc lib module
    public void fetchProperties(ModelProperties properties) {
        logFileConfigurations(properties.getLogFilePath());
        objPropertyReader.propertyRead(properties.getLoincFolderPath(), properties.getElasticSearchHost(), properties.getElasticSearchPort());
    }

}