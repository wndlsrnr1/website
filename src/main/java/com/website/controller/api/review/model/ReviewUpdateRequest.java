package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequest {
    public ReviewUpdateDto toDto() {
        return null;
    }
}

