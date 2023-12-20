package com.website.web.service.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BindingResultUtils {

    private final MessageSource messageSource;

    /**
     * it uses BindingResult.rejectValue Method and set Default Message in this BindingResult's instance
     * @param bindingResult
     * @param field
     * @param code
     * @param args
     * @param locale
     * @return
     */
    public BindingResult addFieldMessagesTo(BindingResult bindingResult, String field, String code, @Nullable Object[] args, Locale locale) {
        String message = messageSource.getMessage(code, args, Locale.KOREA);
        bindingResult.rejectValue(field, code, message);
        return bindingResult;
    }

    public BindingResult addFieldMessagesTo(BindingResult bindingResult, String field, String code, @Nullable Object[] args) {
        return this.addFieldMessagesTo(bindingResult, field, code, args, Locale.KOREA);
    }

    public BindingResult addFieldMessagesTo(BindingResult bindingResult, String field, String code) {
        return this.addFieldMessagesTo(bindingResult, field, code, null, Locale.KOREA);
    }

    /**
     * it uses BindingResult.reject Method and set Default Message in this BindingResult's instance
     * @param bindingResult
     * @param code
     * @param args
     * @param locale
     * @return
     */
    public BindingResult addObjectMessagesTo(BindingResult bindingResult, String code, @Nullable Object[] args, Locale locale){
        String message = messageSource.getMessage(code, args, Locale.KOREA);
        bindingResult.reject(code, args, message);
        return bindingResult;
    }

    public BindingResult addObjectMessagesTo(BindingResult bindingResult, String code, @Nullable Object[] args){
        return this.addObjectMessagesTo(bindingResult, code, args, Locale.KOREA);
    }

    public BindingResult addObjectMessagesTo(BindingResult bindingResult, String code){
        return this.addObjectMessagesTo(bindingResult, code, null, Locale.KOREA);
    }

    /*
    public ApiError getApiError(BindingResult bindingResult, String field, String code, @Nullable Object[] args, Locale locale) {
        this.addFieldMessages(bindingResult, field, code, args, locale);
        return this.getApiError(bindingResult);
    }

    public ApiError getApiError(BindingResult bindingResult, String field, String code, @Nullable Object[] args) {
        this.addFieldMessages(bindingResult, field, code, args);
        return this.getApiError(bindingResult);
    }

    public ApiError getApiError(BindingResult bindingResult, String field, String code) {
        this.addFieldMessages(bindingResult, field, code);
        return this.getApiError(bindingResult);
    }

    public ApiError getApiError(BindingResult bindingResult, String code, @Nullable Object[] args, Locale locale) {
        this.addObjectMessages(bindingResult, code, args, locale);
        return this.getApiError(bindingResult);
    }

    public ApiError getApiError(BindingResult bindingResult, String code, @Nullable Object[] args) {
        this.addObjectMessages(bindingResult, code, args, Locale.KOREA);
        return this.getApiError(bindingResult);
    }

    public ApiError getApiError(BindingResult bindingResult, String code) {
        this.addObjectMessages(bindingResult, code, null, Locale.KOREA);
        return this.getApiError(bindingResult);
    }


    public ApiError getApiError(BindingResult bindingResult) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            log.info("field = {}", fieldError);
        }

        List<String> globalErrors = new ArrayList<>();
        for (ObjectError globalError : bindingResult.getGlobalErrors()) {
            log.info("globalError = {}", globalError);
            globalErrors.add(globalError.getDefaultMessage());
        }

        return ApiError.builder().fieldErrors(fieldErrors).globalErrors(globalErrors).build();
    }
     */

}
