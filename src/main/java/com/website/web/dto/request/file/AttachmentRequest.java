package com.website.web.dto.request.file;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.NONE)
@Getter
public class AttachmentRequest {
    private Long id;
    private String fileName;

    public AttachmentRequest(Long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

}
