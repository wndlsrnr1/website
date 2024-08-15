package com.website.service.review.model;

import com.website.repository.review.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private Long itemId;
    private Long userId;
    private Integer star;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewDto of(Review savedReview) {
        return ReviewDto.builder()
                .id(savedReview.getId())
                .itemId(savedReview.getItem().getId())
                .userId(savedReview.getUser().getId())
                .star(savedReview.getStar())
                .content(savedReview.getContent())
                .createdAt(savedReview.getCreatedAt())
                .updatedAt(savedReview.getUpdatedAt())
                .build();
    }

}
