package cmc.mellyserver.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");


    @Override
    public LocalDateTime convert(String source) {
        if(source == null || source.isEmpty())
        {
            return null;
        }

       return LocalDateTime.parse(source,DATE_TIME_FORMATTER);
    }
}
