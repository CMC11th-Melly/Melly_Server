package cmc.mellyserver.auth.common.filter;

import java.io.IOException;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import cmc.mellyserver.auth.common.util.HeaderUtil;
import cmc.mellyserver.auth.token.TokenProvider;
import cmc.mellyserver.support.exception.LogoutOrWithdrawException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String accessToken = HeaderUtil.getAccessToken(request);

        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {

            // checkLogoutOrWithdrawUser(accessToken);

            Authentication authentication = tokenProvider.getAuthentication(accessToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private void checkLogoutOrWithdrawUser(String accessToken) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        if (Objects.nonNull(valueOperations.get(accessToken))) {
            throw new LogoutOrWithdrawException("already Logout or withdraw user");
        }
    }

}