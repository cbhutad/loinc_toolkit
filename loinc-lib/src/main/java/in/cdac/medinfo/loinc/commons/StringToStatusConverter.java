package in.cdac.medinfo.loinc.commons;

import org.springframework.core.convert.converter.Converter;

public class StringToStatusConverter implements Converter<String, EnumStatus> {

	public EnumStatus convert(String source) {
		return EnumStatus.valueOf(source.toUpperCase());
	}
}
