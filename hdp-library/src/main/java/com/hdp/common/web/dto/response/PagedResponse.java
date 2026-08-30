package com.hdp.common.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.hdp.core.constant.RequestContextConstants.TRACE_ID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PagedResponse<T>(
        boolean success,
        String msg,
        UUID traceId,
        Instant timestamp,
        List<T> data,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious) {

    public PagedResponse {
        if (traceId == null) {
            String raw = MDC.get(TRACE_ID);
            if (raw != null) traceId = UUID.fromString(raw);
        }
        if (timestamp == null) timestamp = Instant.now();
    }

    public static <T> PagedResponse<T> of(List<T> data, int page, int size, long totalElements) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        boolean hasNext = page < totalPages - 1;
        boolean hasPrevious = page > 0;
        return new PagedResponse<>(true, null, null, null, data, page, size, totalElements,
                totalPages, hasNext, hasPrevious);
    }
}
