package com.website.controller.api.model.common;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ApiResponseBody<T> {
    private T data;
    private ApiError error;
    private String message;

    @Builder
    private ApiResponseBody(T data, ApiError apiError, String message) {
        this.data = data;
        this.error = apiError;
        this.message = message;
    }

    public static ApiResponseBody ok() {
        return ApiResponseBody.builder().message("ok").build();
    }

}
