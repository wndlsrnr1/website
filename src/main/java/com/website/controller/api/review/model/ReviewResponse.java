package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
//@NoArgsConstructor
@Data
@Builder
public class ReviewResponse {
    public static ReviewResponse of(ReviewDto reviewDto) {
        return null;
    }
}
