package com.website.web.dto.request.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentUploadRequest {

    private MultipartFile multipartFile;

    private String requestFileName;

}
