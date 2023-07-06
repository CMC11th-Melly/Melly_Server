package cmc.mellyserver.mellycommon.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	@Override
	public LocalDateTime convert(String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		System.out.println("hello" + LocalDateTime.now());
		return LocalDateTime.parse(source, DATE_TIME_FORMATTER);
	}
}
