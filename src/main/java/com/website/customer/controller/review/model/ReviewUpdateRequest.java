package com.website.customer.controller.review.model;

import com.website.customer.service.review.model.ReviewUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequest {
    @NotBlank
    @Length(min = 8, max = 200)
    private String content;
    @Min(value = 1)
    @Max(value = 5)
    private Integer star;

    public ReviewUpdateDto toDto(Long reviewId) {
        return ReviewUpdateDto.builder()
                .reviewId(reviewId)
                .content(content)
                .star(star)
                .build();
    }
}

