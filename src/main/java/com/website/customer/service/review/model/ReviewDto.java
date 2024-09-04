package com.website.customer.service.review.model;

import com.website.common.repository.review.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewDto {
    private Long id;

    private String username;

    private String content;

    private Integer star;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long itemId;

    public static ReviewDto of(Review savedReview) {
        return ReviewDto.builder()
                .id(savedReview.getId())
                .username(savedReview.getPurchases().getUser().getName())
                .content(savedReview.getContent())
                .star(savedReview.getStar())
                .createdAt(savedReview.getCreatedAt())
                .updatedAt(savedReview.getUpdatedAt())
                .itemId(savedReview.getPurchases().getItem().getId())
                .build();
    }
}
