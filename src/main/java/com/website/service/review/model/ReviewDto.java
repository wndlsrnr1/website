package com.website.service.review.model;

import com.website.repository.purchases.model.Purchases;
import com.website.repository.review.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewDto {
    private Long id;

    private Purchases purchases;

    private String content;

    private Integer star;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static ReviewDto of(Review savedReview) {
        return ReviewDto.builder()
                .id(savedReview.getId())
                .purchases(savedReview.getPurchases())
                .content(savedReview.getContent())
                .star(savedReview.getStar())
                .createdAt(savedReview.getCreatedAt())
                .updatedAt(savedReview.getUpdatedAt())
                .build();
    }
}
