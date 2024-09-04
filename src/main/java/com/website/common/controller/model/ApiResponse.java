package com.website.common.controller.model;



import com.website.common.exception.ErrorCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse <T>{

    private final ErrorCode errorCode;
    private final String clientMessage;
    private final T body;

    private ApiResponse() {
        this.errorCode = null;
        this.body = null;
        this.clientMessage = null;
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode, errorCode.getClientMessage(), null);
    }

    public static <T> ApiResponse<T> fail(ErrorCode errorCode, T errorFields) {
        return new ApiResponse<T>(errorCode, errorCode.getClientMessage(), errorFields);
    }

    public static <T> ApiResponse<T> success(T body) {
        return new ApiResponse<>(null, null, body);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(null, null, null);
    }
}
