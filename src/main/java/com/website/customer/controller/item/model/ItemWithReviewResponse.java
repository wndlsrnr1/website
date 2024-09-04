package com.website.customer.controller.item.model;

import com.website.common.service.item.model.ItemWithReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithReviewResponse {

    private Long reviewId;
    private Long itemId;
    private Long imageIdForThumbnail;
    private String itemName;
    private String itemNameKor;
    private String reviewContent;
    private String username;
    private LocalDateTime createdAt;

    public static ItemWithReviewResponse of(ItemWithReviewDto itemWithReview) {
        return ItemWithReviewResponse.builder()
                .reviewId(itemWithReview.getReviewId())
                .itemId(itemWithReview.getItemId())
                .imageIdForThumbnail(itemWithReview.getImageIdForThumbnail())
                .itemName(itemWithReview.getItemName())
                .itemNameKor(itemWithReview.getItemNameKor())
                .reviewContent(itemWithReview.getReviewContent())
                .username(itemWithReview.getUsername())
                .createdAt(itemWithReview.getCreatedAt())
                .build();
    }
}
