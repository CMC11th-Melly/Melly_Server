package cmc.mellyserver.auth.common.util;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeaderUtil {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {

        String headerValue = request.getHeader(HEADER_AUTHORIZATION);

        if (!Objects.isNull(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {

            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

}
