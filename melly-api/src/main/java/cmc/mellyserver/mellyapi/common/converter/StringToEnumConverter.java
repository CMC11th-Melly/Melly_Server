package cmc.mellyserver.mellyapi.common.converter;

import cmc.mellyserver.mellycore.scrap.domain.enums.ScrapType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, ScrapType> {

    @Override
    public ScrapType convert(String source) {
        try {
            return ScrapType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // 만약 빈 값이나 "" 넘어오면 null로 변환해서 조건 사용 가능
        }
    }
}