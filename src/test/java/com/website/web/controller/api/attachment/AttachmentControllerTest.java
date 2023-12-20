package com.website.web.controller.api.attachment;

import com.website.web.dto.request.file.AttachmentUploadRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

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