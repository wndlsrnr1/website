package com.website.web.service.attachment;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

@Slf4j
@SpringBootTest
public class ValidatorInjectionTest {

    @Autowired
    SpringValidatorAdapter springValidatorAdapter;

    @Test
    void 이메일_벨리데이터가_주입되는지 () {
        log.info("springValidatorAdapter = {}", springValidatorAdapter);
        springValidatorAdapter.validate("email").isEmpty();
    }

}
