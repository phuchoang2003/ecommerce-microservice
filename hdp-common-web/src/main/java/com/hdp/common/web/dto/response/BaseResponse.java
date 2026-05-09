package com.hdp.common.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private boolean success;
    private String msg;
    private UUID traceId;
    private Instant timestamp;

    public BaseResponse() {
    }

    public BaseResponse(boolean success, String msg, UUID traceId, Instant timestamp) {
        this.success = success;
        this.msg = msg;
        this.traceId = traceId;
        this.timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public static BaseResponse success() {
        return new BaseResponse(true, null, null, Instant.now());
    }

    public static BaseResponse success(String message) {
        return new BaseResponse(true, message, null, Instant.now());
    }

    public void success(boolean success) {
        this.success = success;
    }

    public String message() {
        return msg;
    }

    public void message(String message) {
        this.msg = message;
    }

    public UUID traceId() {
        return traceId;
    }

    public void traceId(UUID traceId) {
        this.traceId = traceId;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public void timestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}