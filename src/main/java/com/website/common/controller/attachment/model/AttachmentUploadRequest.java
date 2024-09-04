package com.website.common.controller.attachment.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentUploadRequest {

    private MultipartFile multipartFile;

    private String requestFileName;

}
