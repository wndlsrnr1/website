package com.website.common.controller.attachment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.MalformedURLException;

@RestControllerAdvice(basePackageClasses = AttachmentController.class)
@Slf4j
public class AttachmentControllerAdvice {

    @ExceptionHandler(MalformedURLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleMalFormedURLException(MalformedURLException e) {
        log.error("[handleMalformedURLException in com.website.web.controller.api.attachment.AttachmentControllerAdvice", e);
        return ResponseEntity.badRequest().body("파일 형식을 확인하세요");
    }


}
