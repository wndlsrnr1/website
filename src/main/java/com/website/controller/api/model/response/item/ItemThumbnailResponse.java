package com.website.controller.api.model.response.item;

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
