package com.website.exception;


import com.website.controller.api.common.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonRestControllerAdvice {

    // todo: implement method by ClientException, MethodArgumentNotValidException, ServerException, Exception when it is in need

    @ExceptionHandler(value = {ClientException.class})
    public ResponseEntity<ApiResponse<Void>> handleClientException(ClientException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(exception.getServerMessage(), exception);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode));
    }

    @ExceptionHandler(value = {
            UnauthorizedActionException.class,
            DateExpiredException.class})
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedActionException(
            CommonException exception
    ) {
        log.warn(exception.getServerMessage(), exception);
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode));
    }



}
