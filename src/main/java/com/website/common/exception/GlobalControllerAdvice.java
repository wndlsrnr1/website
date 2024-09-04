package com.website.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Locale;

@RestControllerAdvice(basePackages = "com/website/controller/api")
@RequiredArgsConstructor
@Slf4j
public class GlobalControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity handleTypeMisMatchException(JsonMappingException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("[handleTypeMisMatchException]", e);
        Locale locale = request.getLocale();
        String message = messageSource.getMessage("TypeMismatch", null, locale);
        return ResponseEntity.badRequest().body(message);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleInternalServerError(RuntimeException e, HttpServletRequest request) {
        log.error("[InternalServerError]", e);
        Locale locale = request.getLocale();
        String message = messageSource.getMessage("InternalServerError", null, locale);
        return ResponseEntity.internalServerError().body(message);
    }

}
