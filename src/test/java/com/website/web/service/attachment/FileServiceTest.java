package com.website.web.service.attachment;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class FileServiceTest {

    @Test
    void subStringTest() {
        String s = "sadfasdf.asdfasdfasf";
        String substring = s.substring(0, 8);
        log.info(substring);
    }

    @Test
    public void LDT() {
        String s = LocalDateTime.now().toString();
        System.out.println("s = " + s);
    }

    //@Test
    //void insertFileOne() {
    //    System.out.println("UUID.to = " + UUID.randomUUID().toString());
    //}
}