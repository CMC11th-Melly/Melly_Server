package cmc.mellyserver.auth.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HeaderUtil {

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request)
    {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);

        if(headerValue == null) {
            return null;
        }

        if(headerValue.startsWith(TOKEN_PREFIX))
        {

            String substring = headerValue.substring(TOKEN_PREFIX.length());

            if(substring.contains("null") || substring == null)
            {
                return null;
            }
            else{
                return substring;
            }

        }
        return null;
    }
}
