package com.example.library.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("dev")
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = Instant.now().toEpochMilli();

        // Wrap both request & response so we can read bodies after execution
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = Instant.now().toEpochMilli() - startTime;

            logRequestDetails(requestWrapper);
            logResponseDetails(responseWrapper, duration);

            // Important: copy the cached response body back to the actual response output
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequestDetails(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        String body = "";
        try {
            byte[] buf = request.getContentAsByteArray();
            if (buf.length > 0) {
                body = new String(buf, StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) { }

        String formattedBody = prettyJson(body);
        log.info("Request: [{} {}]\nBody:\n{}", method, uri, formattedBody);
    }

    private void logResponseDetails(ContentCachingResponseWrapper response, long duration) {
        int status = response.getStatus();

        String body = "";
        try {
            byte[] buf = response.getContentAsByteArray();
            if (buf.length > 0) {
                body = new String(buf, StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) { }

        String formattedBody = prettyJson(body);
        log.info("Response: [Status: {}] [Time: {} ms]\nBody:\n{}", status, duration, formattedBody);
    }

    private String prettyJson(String json) {
        if (json == null || json.isBlank()) return "(no body)";
        try {
            Object parsed = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(parsed);
        } catch (Exception e) {
            return json;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
      String path = request.getRequestURI();
      // Exclude Swagger
      return path.startsWith("/swagger") ||
             path.startsWith("/v3/api-docs");
    }
}
