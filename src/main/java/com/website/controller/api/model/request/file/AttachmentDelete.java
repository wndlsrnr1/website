package com.website.controller.api.model.request.file;

import lombok.Data;

@Data
public class AttachmentDelete {
    private Long fileId;
    private String requestName;
    private String savedFileName;
}
