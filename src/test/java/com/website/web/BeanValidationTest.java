package com.website.web;

import com.website.web.dto.request.user.JoinFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Locale;
import java.util.Set;

@Slf4j
@SpringBootTest
class BeanValidationTest {

    @Autowired
    MessageSource messageSource;

    @Test
    void testNotEmpty() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        JoinFormRequest joinFormRequest = new JoinFormRequest();
        joinFormRequest.setEmail("");
        joinFormRequest.setAddress(" ");
        joinFormRequest.setName("   ");
        joinFormRequest.setPassword("  ");
        joinFormRequest.setPassword2("   ");
        Set<ConstraintViolation<JoinFormRequest>> validate = validator.validate(joinFormRequest);
        log.info("validate = {}", validate);
        for (ConstraintViolation<JoinFormRequest> violation : validate) {
            String message = violation.getMessage();
            log.info("message = {}", message);
            String notEmpty = this.messageSource.getMessage("javax.validation.constraints.NotBlank.message", null, Locale.KOREA);
            Assertions.assertThat(message).isEqualTo(notEmpty);
        }
    }


}