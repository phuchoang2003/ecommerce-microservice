package com.hdp.common.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.UUID;

import static com.hdp.core.constant.RequestContextConstants.TRACE_ID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String msg,
        UUID traceId,
        Instant timestamp,
        T data) {

    public ApiResponse {
        if (traceId == null) {
            String raw = MDC.get(TRACE_ID);
            if (raw != null) traceId = UUID.fromString(raw);
        }
        if (timestamp == null) timestamp = Instant.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, null, null, data);
    }

    public static <T> ApiResponse<T> success(T data, String msg) {
        return new ApiResponse<>(true, msg, null, null, data);
    }
}
