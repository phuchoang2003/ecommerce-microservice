package com.hdp.common.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.hdp.core.constant.RequestContextConstants.TRACE_ID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CursorResponse<T>(
        boolean success,
        String msg,
        UUID traceId,
        Instant timestamp,
        List<T> data,
        String nextCursor,
        boolean hasMore) {

    public CursorResponse {
        if (traceId == null) {
            String raw = MDC.get(TRACE_ID);
            if (raw != null) traceId = UUID.fromString(raw);
        }
        if (timestamp == null) timestamp = Instant.now();
    }

    public static <T> CursorResponse<T> of(List<T> data, String nextCursor, boolean hasMore) {
        return new CursorResponse<>(true, null, null, null, data, nextCursor, hasMore);
    }
}
