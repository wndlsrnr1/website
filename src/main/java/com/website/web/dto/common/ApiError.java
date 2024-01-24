package com.website.web.dto.common;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class ApiError {
    private Map<String, String> fieldErrors;
    private List<String> globalErrors;

    @Builder
    private ApiError(Map<String, String> fieldErrors, List<String> globalErrors) {
        this.fieldErrors = fieldErrors;
        this.globalErrors = globalErrors;
    }

    public ApiError(BindingResult bindingResult) {
        this.fieldErrors = getFieldErrors(bindingResult);
        this.globalErrors = getGlobalErrors(bindingResult);
    }

    public ApiError(String field, String message) {
        this.fieldErrors = Map.of(field, message);
    }


    private List<String> getGlobalErrors(BindingResult bindingResult) {
        List<String> globalErrors = new ArrayList<>();
        for (ObjectError globalError : bindingResult.getGlobalErrors()) {
            globalErrors.add(globalError.getDefaultMessage());
        }
        return globalErrors;
    }

    private Map<String, String> getFieldErrors(BindingResult bindingResult) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return fieldErrors;
    }

}
