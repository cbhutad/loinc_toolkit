package in.cdac.medinfo.loinc.commons;

import org.springframework.core.convert.converter.Converter;

public class StringToClassTypesConverter implements Converter<String, EnumClassTypes> {

	public EnumClassTypes convert(String source) {
		return EnumClassTypes.valueOf(source.toUpperCase());
	}

}
