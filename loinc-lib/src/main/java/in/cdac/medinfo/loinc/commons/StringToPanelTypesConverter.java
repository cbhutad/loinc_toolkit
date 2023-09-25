package in.cdac.medinfo.loinc.commons;

import org.springframework.core.convert.converter.Converter;

public class StringToPanelTypesConverter implements Converter<String, EnumPanelTypes> {

	public EnumPanelTypes convert(String source) {
		return EnumPanelTypes.valueOf(source.toUpperCase());
	}

}
