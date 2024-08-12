package com.website.service.review.model;

import com.website.repository.review.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDeleteDto {
    private Long userId;
    private Long reviewId;

}
