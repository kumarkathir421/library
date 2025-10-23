package com.example.library.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class TracingFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            //Generate IDs // Integrate Spring Cloud Sleuth + Zipkin (auto-distributes trace/span IDs across microservices)

            String traceId = UUID.randomUUID().toString().substring(0, 12);
            String spanId = UUID.randomUUID().toString().substring(0, 8);

            //Put into MDC (thread-local context)
            MDC.put(TRACE_ID, traceId);
            MDC.put(SPAN_ID, spanId);

            // Also send them as HTTP response headers (for client correlation)
            response.setHeader("X-Trace-Id", traceId);
            response.setHeader("X-Span-Id", spanId);

            log.debug("Trace Started → traceId={}, spanId={}, URI={}", traceId, spanId, request.getRequestURI());

            // Continue filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Clean up MDC to avoid leaking IDs across threads
            MDC.clear();
        }
    }
}
