package cmc.mellyserver.common.filter;

import cmc.mellyserver.common.token.AuthToken;
import cmc.mellyserver.common.token.JwtTokenProvider;
import cmc.mellyserver.common.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {


        String tokenStr = HeaderUtil.getAccessToken(request);

        if(tokenStr == "" || tokenStr != null )
        {
            // 레디스에 토큰 값 없음 -> 로그아웃 되지 않는 토큰
            if(redisTemplate.opsForValue().get(tokenStr) == null)
            {
                AuthToken token = tokenProvider.convertAuthToken(tokenStr);

                if (token.validate()) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            else{
                log.info("로그아웃 된 엑세스 토큰입니다.");
            }
        }

        filterChain.doFilter(request, response);
    }

}