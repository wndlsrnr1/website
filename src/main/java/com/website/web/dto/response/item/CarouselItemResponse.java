package com.website.web.dto.response.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarouselItemResponse {

    private Long id;

    private Long itemId;
    private String itemName;
    private String itemNameKor;

    private Long attachmentId;
    private String savedNameOfAttachment;
    private String requestedNameOfAttachment;

    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public CarouselItemResponse(Long id, Long itemId, String itemName, String itemNameKor, Long attachmentId, String savedNameOfAttachment, String requestedNameOfAttachment, Integer priority, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemNameKor = itemNameKor;
        this.attachmentId = attachmentId;
        this.savedNameOfAttachment = savedNameOfAttachment;
        this.requestedNameOfAttachment = requestedNameOfAttachment;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
