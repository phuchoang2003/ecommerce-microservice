package com.hdp.common.web.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse<T> extends BaseResponse {
    @JsonProperty("data") private T data;

    public ApiResponse() {
    }

    public ApiResponse(T data) {
        super(true, null, null, null);
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> success(T data, String msg) {
        ApiResponse<T> response = new ApiResponse<>(data);
        response.setMsg(msg);
        return response;
    }


    public T getData() { return data; }


    public void setData(T data) { this.data = data; }
}