package cmc.mellyserver.mellyapi.common.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmc.mellyserver.mellyapi.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import static cmc.mellyserver.mellycore.common.exception.ErrorCode.FORBIDDEN;
import static cmc.mellyserver.mellycore.common.exception.ErrorCode.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class TokenAccessDeniedHandler implements AccessDeniedHandler {

	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		ErrorResponse error = ErrorResponse.of(FORBIDDEN.getCode(), accessDeniedException.getMessage());

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), error);

		handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
	}
}