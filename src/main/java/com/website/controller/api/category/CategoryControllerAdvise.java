package com.website.controller.api.category;

import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = {CategoryController.class, SubcategoryController.class})
@Slf4j
public class CategoryControllerAdvise {

    @ExceptionHandler(JsonMappingException.class)
    public void malFormJsonResponse(JsonMappingException e) {
        log.error("[response 할 수 없는 형식]", e);
    }

}
