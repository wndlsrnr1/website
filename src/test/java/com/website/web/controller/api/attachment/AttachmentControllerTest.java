package com.website.web.controller.api.attachment;

import com.website.controller.api.attachment.AttachmentController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
class AttachmentControllerTest {

    private final AttachmentController controller;

    @Value("${file.dir}")
    private String fileDir;

    @Test
    void responseFileToUser() {
    }


}