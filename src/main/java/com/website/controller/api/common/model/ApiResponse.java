package com.website.controller.api.common.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.website.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse <T>{

    //@JsonSerialize(using = Serializer)
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

    public static <T> ApiResponse<T> success(T body) {
        return new ApiResponse<>(null, null, body);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(null, null, null);
    }
}
