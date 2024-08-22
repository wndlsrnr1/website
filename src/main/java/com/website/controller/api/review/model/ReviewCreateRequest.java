package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@Builder
public class ReviewCreateRequest {
    private Integer star;
    private String content;
    private Long purchasesId;
    public ReviewCreateDto toDto() {
        return ReviewCreateDto.builder()
                .star(star)
                .content(content)
                .purchasesId(purchasesId)
                .build();
    }
}
