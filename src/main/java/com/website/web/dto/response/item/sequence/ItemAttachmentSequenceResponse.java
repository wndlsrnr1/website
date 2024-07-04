package com.website.web.dto.response.item.sequence;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ItemAttachmentSequenceResponse {
    private Long fileId;
    private Integer sequence;

    @QueryProjection
    public ItemAttachmentSequenceResponse(Long fileId, Integer sequence) {
        this.fileId = fileId;
        this.sequence = sequence;
    }
}
