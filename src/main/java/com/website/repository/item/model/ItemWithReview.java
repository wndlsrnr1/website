package com.website.repository.item.model;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@Data
public class ItemWithReview {
    private Long reviewId;
    private Long itemId;
    private Long imageIdForThumbnail;
    private String itemName;
    private String itemNameKor;
    private String reviewContent;
    private String username;
    private LocalDateTime createdAt;

    @QueryProjection
    public ItemWithReview(Long reviewId, Long itemId, Long imageIdForThumbnail, String itemName, String itemNameKor, String reviewContent, String username, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.itemId = itemId;
        this.imageIdForThumbnail = imageIdForThumbnail;
        this.itemName = itemName;
        this.itemNameKor = itemNameKor;
        this.reviewContent = reviewContent;
        this.username = username;
        this.createdAt = createdAt;
    }
}
