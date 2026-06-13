package com.hdp.common.web.filter;

import com.hdp.core.constant.RequestContextConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(RequestResponseLoggingProperties.class)
public class HttpRequestResponseLoggingFilter extends OncePerRequestFilter {

    private final RequestResponseLoggingProperties properties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public HttpRequestResponseLoggingFilter(RequestResponseLoggingProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        if (Boolean.FALSE.equals(properties.enabled())) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestUri = request.getRequestURI();
        String path = stripContextPath(requestUri, request.getContextPath());
        if (!shouldLog(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = Instant.now().toEpochMilli();

        String traceId = getOrGenerateTraceId(request);
        response.setHeader(RequestContextConstants.HEADER_TRACE_ID, traceId);

        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(request, wrappedResponse);
        } finally {
            int status = wrappedResponse.getStatus();
            long duration = Instant.now().toEpochMilli() - startTime;
            String body = getResponseBody(wrappedResponse);

            log.info("{} {} - {}ms - status={} | body={}",
                    request.getMethod(), requestUri, duration, status, truncate(body));

            wrappedResponse.copyBodyToResponse();
        }
    }

    private boolean shouldLog(String path) {
        if (properties.excludePatterns().stream().anyMatch(pattern -> pathMatcher.match(pattern, path))) {
            return false;
        }
        return properties.includePatterns().isEmpty() ||
               properties.includePatterns().stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private String stripContextPath(String uri, String contextPath) {
        if (contextPath == null || contextPath.isEmpty() || "/".equals(contextPath)) {
            return uri;
        }
        return uri.startsWith(contextPath) ? uri.substring(contextPath.length()) : uri;
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        return new String(buf, StandardCharsets.UTF_8);
    }

    private String truncate(String body) {
        if (body == null || body.isBlank()) {
            return body;
        }
        int maxLength = properties.maxBodyLength();
        if (body.length() > maxLength) {
            return body.substring(0, maxLength) + "...[truncated]";
        }
        return body;
    }

    private String getOrGenerateTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(RequestContextConstants.HEADER_TRACE_ID);
        return traceId != null && !traceId.isBlank() ? traceId : UUID.randomUUID().toString();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !properties.enabled();
    }
}