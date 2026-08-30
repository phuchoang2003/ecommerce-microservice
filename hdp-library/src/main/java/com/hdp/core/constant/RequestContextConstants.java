package com.hdp.core.constant;

public final class RequestContextConstants {

    private RequestContextConstants() {
    }

    public static final String TRACE_ID = "trace_id";
    public static final String SPAN_ID = "span_id";
    public static final String USER_ID = "user_id";
    public static final String REQUEST_ID = "request_id";
    public static final String SERVICE = "service";
    public static final String ENDPOINT = "endpoint";
    public static final String METHOD = "method";
    public static final String EVENT = "event";
    public static final String DATA = "data";
    public static final String DURATION = "duration_ms";
    public static final String ERROR_CODE = "error_code";
    public static final String TIMESTAMP = "timestamp";
    public static final String ERROR_DETAIL = "error_detail";

    public static final String HEADER_TRACE_ID = "X-Trace-Id";
    public static final String HEADER_REQUEST_ID = "X-Request-Id";
    public static final String HEADER_USER_ID = "X-User-Id";
}