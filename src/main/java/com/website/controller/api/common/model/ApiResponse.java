package com.website.controller.api.common.model;


import com.website.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse <T>{

    private final ErrorCode errorCode;
    private final T body;

    private ApiResponse() {
        this.errorCode = null;
        this.body = null;
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
