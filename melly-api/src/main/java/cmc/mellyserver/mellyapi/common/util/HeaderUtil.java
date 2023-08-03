package cmc.mellyserver.mellyapi.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
public class HeaderUtil {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {

        String headerValue = request.getHeader(HEADER_AUTHORIZATION); // Bearer token

        if (!Objects.isNull(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {

            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

}
