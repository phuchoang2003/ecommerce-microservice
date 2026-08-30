package com.hdp.common.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    @JsonProperty("success") private boolean success;
    @JsonProperty("msg") private String msg;
    @JsonProperty("traceId") private UUID traceId;
    @JsonProperty("timestamp") private Instant timestamp;

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
    public void setMsg(String msg) { this.msg = msg; }

    public boolean isSuccess() { return success; }
    public String getMsg() { return msg; }
    public UUID getTraceId() { return traceId; }
    public Instant getTimestamp() { return timestamp; }
}