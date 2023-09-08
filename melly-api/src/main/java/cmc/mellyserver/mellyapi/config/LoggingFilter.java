package cmc.mellyserver.mellyapi.config;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestId = ((HttpServletRequest) request).getHeader("X-RequestID");
        MDC.put("request_id", Optional.ofNullable(requestId).orElse(UUID.randomUUID().toString().replaceAll("-", "")));
        chain.doFilter(request, response);
        MDC.clear();
    }
}
