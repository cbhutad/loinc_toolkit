package in.cdac.medinfo.loinc;

import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import in.cdac.medinfo.loinc.commons.StringToClassTypesConverter;
import in.cdac.medinfo.loinc.commons.StringToPanelTypesConverter;
import in.cdac.medinfo.loinc.commons.StringToStatusConverter;

/**
 * 
 * This class registers all Enum Converters
 */

@Component
public class WebFormatters implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToClassTypesConverter());
        registry.addConverter(new StringToPanelTypesConverter());
        registry.addConverter(new StringToStatusConverter());
    }

}