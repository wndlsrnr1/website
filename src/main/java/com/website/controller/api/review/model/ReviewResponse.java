package com.website.controller.api.review.model;

import com.website.repository.purchases.model.Purchases;
import com.website.service.review.model.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewResponse {
    private Long id;

    private Long purchasesId;

    private String content;

    private Integer star;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ReviewResponse of(ReviewDto dto) {
        return ReviewResponse.builder()
                .id(dto.getId())
                .purchasesId(dto.getPurchasesId())
                .content(dto.getContent())
                .star(dto.getStar())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
