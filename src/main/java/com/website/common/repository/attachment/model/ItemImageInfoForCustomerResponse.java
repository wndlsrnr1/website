package com.website.common.repository.attachment.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ItemImageInfoForCustomerResponse {
    private Long itemId;
    private Long imageId;
    private String originalName;
    private String savedName;
    private Boolean isThumbnail;
    private Integer sequence;

    @QueryProjection
    public ItemImageInfoForCustomerResponse(Long itemId, Long imageId, String originalName, String savedName, Boolean isThumbnail, Integer sequence) {
        this.itemId = itemId;
        this.imageId = imageId;
        this.originalName = originalName;
        this.savedName = savedName;
        this.isThumbnail = isThumbnail;
        this.sequence = sequence;
    }
}
