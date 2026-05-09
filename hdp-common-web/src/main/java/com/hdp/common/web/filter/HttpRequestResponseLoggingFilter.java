package com.hdp.common.web.filter;


import com.hdp.core.constant.RequestContextConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@AutoConfiguration
@EnableConfigurationProperties(RequestResponseLoggingProperties.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpRequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestResponseLoggingFilter.class);

    private final RequestResponseLoggingProperties properties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper;

    public HttpRequestResponseLoggingFilter(RequestResponseLoggingProperties properties,
                                            ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!properties.enabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        if (!shouldLog(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        long startTime = System.currentTimeMillis();

        String traceId = getOrGenerateTraceId(request);
        String requestId = UUID.randomUUID().toString();

        MDC.put(RequestContextConstants.TRACE_ID, traceId);
        MDC.put(RequestContextConstants.REQUEST_ID, requestId);
        MDC.put(RequestContextConstants.ENDPOINT, path);
        MDC.put(RequestContextConstants.METHOD, request.getMethod());
        MDC.put(RequestContextConstants.SERVICE, getServiceName());

        response.setHeader(RequestContextConstants.HEADER_TRACE_ID, traceId);
        response.setHeader(RequestContextConstants.HEADER_REQUEST_ID, requestId);

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        CachedBodyHttpServletResponse cachedResponse = new CachedBodyHttpServletResponse(response);

        try {
            filterChain.doFilter(cachedRequest, cachedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            logRequest(cachedRequest, traceId, requestId);
            logResponse(cachedResponse, traceId, requestId);

            log.info("{} {} - {}ms - status={}", request.getMethod(), path, duration, cachedResponse.getStatus());

            MDC.clear();
        }
    }

    private boolean shouldLog(String path) {
        if (properties.excludePatterns().stream().anyMatch(pattern -> pathMatcher.match(pattern, path))) {
            return false;
        }
        return properties.includePatterns().stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private void logRequest(CachedBodyHttpServletRequest request, String traceId, String requestId) {
        if (!properties.logRequestBody()) {
            log.info("--> {} {}", request.getMethod(), request.getRequestURI());
            return;
        }

        String body = readBody(request);
        if (body != null && !body.isBlank()) {
            String truncatedBody = truncate(body);
            log.info("--> {} {} | body={}", request.getMethod(), request.getRequestURI(), truncatedBody);
        } else {
            log.info("--> {} {}", request.getMethod(), request.getRequestURI());
        }
    }

    private void logResponse(CachedBodyHttpServletResponse response, String traceId, String requestId) {
        if (!properties.logResponseBody()) {
            return;
        }

        String body = response.getBody();
        if (body != null && !body.isBlank()) {
            String truncatedBody = truncate(body);
            log.info("<-- {} | body={}", response.getStatus(), truncatedBody);
        }
    }

    private String readBody(CachedBodyHttpServletRequest request) {
        try {
            return new String(request.getCachedBody(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    private String truncate(String body) {
        if (body.length() > properties.maxBodyLength()) {
            return body.substring(0, properties.maxBodyLength()) + "...[truncated]";
        }
        Object json = objectMapper.readValue(body, Object.class);
        return objectMapper.writeValueAsString(json);
    }

    private String getOrGenerateTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(RequestContextConstants.HEADER_TRACE_ID);
        return traceId != null && !traceId.isBlank() ? traceId : UUID.randomUUID().toString();
    }

    private String getServiceName() {
        String serviceName = System.getenv("SERVICE_NAME");
        return serviceName != null ? serviceName : "unknown-service";
    }

    private static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

        private final byte[] cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            this.cachedBody = request.getInputStream().readAllBytes();
        }

        public byte[] getCachedBody() {
            return cachedBody;
        }

        @Override
        public ServletInputStream getInputStream() {
            return new CachedBodyServletInputStream(this.cachedBody);
        }

        @Override
        public BufferedReader getReader() {
            ByteArrayInputStream bais = new ByteArrayInputStream(this.cachedBody);
            return new BufferedReader(new InputStreamReader(bais, StandardCharsets.UTF_8));
        }
    }

    private static class CachedBodyServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.inputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() {
            return inputStream.read();
        }
    }

    private static class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        @Getter
        private int status = HttpStatus.OK.value();
        private String body;

        public CachedBodyHttpServletResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() {
            return new PrintWriter(outputStream, true, StandardCharsets.UTF_8);
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return new CachedBodyServletOutputStream(outputStream);
        }

        @Override
        public void setStatus(int status) {
            this.status = status;
            super.setStatus(status);
        }

        @Override
        public void sendError(int sc) throws IOException {
            this.status = sc;
            super.sendError(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            this.status = sc;
            super.sendError(sc, msg);
        }

        public String getBody() {
            if (body == null) {
                body = outputStream.toString(StandardCharsets.UTF_8);
            }
            return body;
        }
    }

    private static class CachedBodyServletOutputStream extends ServletOutputStream {

        private final OutputStream outputStream;

        public CachedBodyServletOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b, off, len);
        }
    }
}