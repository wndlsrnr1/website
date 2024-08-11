package com.website.controller.api.model.request.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentUploadRequest {

    private MultipartFile multipartFile;

    private String requestFileName;

}
