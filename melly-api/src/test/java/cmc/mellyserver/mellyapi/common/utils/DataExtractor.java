package cmc.mellyserver.mellyapi.common.utils;

import cmc.mellyserver.mellyapi.common.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataExtractor {

    private final ObjectMapper objectMapper;

    public String extractDataField(String body) throws JsonProcessingException {
        return objectMapper.writeValueAsString(objectMapper.readValue(body, ApiResponse.class).getData());
    }
}
