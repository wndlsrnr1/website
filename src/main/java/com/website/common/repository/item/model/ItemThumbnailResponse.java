package com.website.common.repository.item.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ItemThumbnailResponse {
    private Long itemThumbnailId;
    private Long fileId;
    private Long itemId;

    @QueryProjection
    public ItemThumbnailResponse(Long itemThumbnailId, Long fileId, Long itemId) {
        this.itemThumbnailId = itemThumbnailId;
        this.fileId = fileId;
        this.itemId = itemId;
    }
}
