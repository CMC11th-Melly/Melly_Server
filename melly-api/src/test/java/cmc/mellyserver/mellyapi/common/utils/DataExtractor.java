package cmc.mellyserver.mellyapi.common.utils;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cmc.mellyserver.mellyapi.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataExtractor {

	private final ObjectMapper objectMapper;

	public String extractDataField(String body) throws JsonProcessingException {
		return objectMapper.writeValueAsString(objectMapper.readValue(body, CommonResponse.class).getData());
	}
}
