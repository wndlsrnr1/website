package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequest {
    private Long reviewId;
    private String content;
    private Integer star;

    public ReviewUpdateDto toDto() {
        return ReviewUpdateDto.builder()
                .reviewId(reviewId)
                .content(content)
                .star(star)
                .build();
    }
}

