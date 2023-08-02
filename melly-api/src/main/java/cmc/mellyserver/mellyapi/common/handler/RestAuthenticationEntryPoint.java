package cmc.mellyserver.mellyapi.common.handler;

import cmc.mellyserver.mellyapi.common.response.ErrorResponse;
import cmc.mellyserver.mellycommon.codes.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse error = ErrorResponse.of(ErrorCode.UNAUTHORIZED_USER.getCode(), authException.getMessage());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), error);
    }
}
