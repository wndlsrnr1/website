package com.website.web.dto.response;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class APIResponseEntityTest {

    @Test
    void testAPIResponseEntity() {
        //APIResponseEntity<Date> build = APIResponseEntity.<Date>builder().body(new Date()).httpStatus(HttpStatus.OK).build();
        //log.info("build = {}", build);
    }

}