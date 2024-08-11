package com.website.controller.api.model.response.item;

import lombok.Data;

import java.io.File;

@Data
public class AttachmentByItemIdResponse {

    private Long attachmentId;
    private String saveName;
    private String requestName;
    private File file;

    public AttachmentByItemIdResponse(Long attachmentId, String saveName, String requestName, File file) {
        this.attachmentId = attachmentId;
        this.saveName = saveName;
        this.requestName = requestName;
        this.file = file;
    }
}
