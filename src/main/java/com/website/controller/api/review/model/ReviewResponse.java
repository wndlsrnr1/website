package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long itemId;
    private Long userId;
    private Integer star;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponse of(ReviewDto reviewDto) {
        return ReviewResponse.builder()
                .id(reviewDto.getId())
                .itemId(reviewDto.getItemId())
                .userId(reviewDto.getUserId())
                .star(reviewDto.getStar())
                .content(reviewDto.getContent())
                .createdAt(reviewDto.getCreatedAt())
                .updatedAt(reviewDto.getUpdatedAt())
                .build();
    }
}
