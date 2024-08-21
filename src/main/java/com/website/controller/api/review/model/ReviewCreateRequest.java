package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Builder
public class ReviewCreateRequest {
    public ReviewCreateDto toDto(Long userId) {
        return null;
    }
}
