package com.hdp.common.web.dto.response;

public class ApiResponse<T> extends BaseResponse {
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(T data) {
        super(true, null, null, null);
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(data);
        response.message(message);
        return response;
    }

    public T data() { return data; }
    public void data(T data) { this.data = data; }
}