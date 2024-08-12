package com.website.controller.api.common.model;


import com.website.exception.ErrorCode;

public class ApiResponse <T>{

    private ErrorCode errorCode;
    private T body;

    private ApiResponse(ErrorCode errorCode, T body) {
        this.errorCode = errorCode;
        this.body = body;
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode, null);
    }

    public static <T> ApiResponse<T> success(T body) {
        return new ApiResponse<>(null, body);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(null, null);
    }
}
