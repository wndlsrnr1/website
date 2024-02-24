package com.website.web.dto.request.file;

import lombok.Data;

@Data
public class AttachmentDelete {
    private Long fileId;
    private String requestName;
    private String savedFileName;
}
