package com.collabera.library.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@Profile("dev")
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // skip non-API routes (to avoid logging HTML, JS, etc.)
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // assign trace/span
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        String spanId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(TRACE_ID, traceId);
        MDC.put(SPAN_ID, spanId);

        // wrap request/response to read body
        var reqWrapper = new ContentCachingRequestWrapper(request);
        var resWrapper = new ContentCachingResponseWrapper(response);

        var start = Instant.now();

        try {
            filterChain.doFilter(reqWrapper, resWrapper);
        } finally {
            long timeMs = Duration.between(start, Instant.now()).toMillis();

            logRequest(reqWrapper, traceId, spanId);
            logResponse(resWrapper, traceId, spanId, timeMs);

            resWrapper.copyBodyToResponse(); // required
            MDC.clear();
        }
    }

    private void logRequest(ContentCachingRequestWrapper req, String traceId, String spanId) {
        String body = getBody(req.getContentAsByteArray());
        log.info("\n📥 Request [{} {}] | traceId={} spanId={}\n{}\n",
                req.getMethod(),
                getFullUrl(req),
                traceId,
                spanId,
                prettyJson(body));
    }

    private void logResponse(ContentCachingResponseWrapper res, String traceId, String spanId, long timeMs) {
        String body = getBody(res.getContentAsByteArray());
        log.info("\n📤 Response [{}] | traceId={} spanId={} | {} ms\n{}\n",
                res.getStatus(),
                traceId,
                spanId,
                timeMs,
                prettyJson(body));
    }

    private String getFullUrl(HttpServletRequest request) {
        String query = request.getQueryString();
        return query == null ? request.getRequestURI() : request.getRequestURI() + "?" + query;
    }

    private String getBody(byte[] content) {
        return content.length == 0 ? "" : new String(content, StandardCharsets.UTF_8);
    }

    private String prettyJson(String text) {
        if (text == null || text.isBlank()) return "(no body)";
        try {
            Object json = mapper.readValue(text, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            // not JSON → print raw
            return text.length() > 500 ? text.substring(0, 500) + "..." : text;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !uri.startsWith("/api") ||
               uri.startsWith("/swagger") ||
               uri.startsWith("/v3") ||
               uri.contains("/webjars");
    }

}