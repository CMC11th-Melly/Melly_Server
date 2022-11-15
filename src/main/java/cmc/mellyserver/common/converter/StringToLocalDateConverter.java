package cmc.mellyserver.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");


    @Override
    public LocalDate convert(String source) {
        if(source == null || source.isEmpty())
        {
            return null;
        }

        return LocalDate.parse(source,DATE_TIME_FORMATTER);
    }
}
