package cmc.mellyserver.common.filter;

import java.io.IOException;
import java.util.UUID;

import org.jboss.logging.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MDCLoggingFilter implements Filter {
    private static final String MDC_KEY = "request_id";

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
        final FilterChain filterChain) throws ServletException, IOException {
        final UUID uuid = UUID.randomUUID();
        MDC.put(MDC_KEY, uuid.toString());
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }
}
