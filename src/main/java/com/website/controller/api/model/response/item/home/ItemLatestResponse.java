package com.website.controller.api.model.response.item.home;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemLatestResponse {
    private Long id;
    private String name;
    private LocalDateTime releasedAt;
    private Integer price;
    private Integer discountRatio;
    private Long thumbnailId;
    private Long fileId;

    @QueryProjection
    public ItemLatestResponse(Long id, String name, LocalDateTime releasedAt, Integer price, Integer discountRatio, Long thumbnailId, Long fileId) {
        this.id = id;
        this.name = name;
        this.releasedAt = releasedAt;
        this.price = price;
        this.discountRatio = discountRatio;
        this.thumbnailId = thumbnailId;
        this.fileId = fileId;
    }

}
