package com.website.exception;


import com.website.controller.api.common.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CommonRestControllerAdvice {

    // todo: implement method by ClientException, ServerException, Exception when it is in need
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = extractFieldErrors(ex);
        log.warn("Validation failed.", ex);
        log.info("errors = {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(ErrorCode.BAD_REQUEST, errors));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(
            BadCredentialsException ex
    ) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.fail(ErrorCode.FORBIDDEN));
    }


    @ExceptionHandler(value = {ClientException.class})
    public ResponseEntity<ApiResponse<Void>> handleClientException(ClientException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn(exception.getServerMessage(), exception);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode));
    }

    @ExceptionHandler(value = {
            UnauthorizedActionException.class,
            DateExpiredException.class,
            ResourceNotFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedActionException(
            CommonException exception
    ) {
        log.warn(exception.getServerMessage(), exception);
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode));
    }

    private Map<String, String> extractFieldErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage,
                        (existingValue, newValue) -> existingValue // If there are multiple errors, keep the first one
                ));
    }



}
