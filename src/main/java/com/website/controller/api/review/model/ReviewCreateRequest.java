package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateRequest {
    private Long itemId;
    private Integer star;
    private String content;

    public ReviewCreateDto toDto(Long userId) {
        return ReviewCreateDto.builder()
                .itemId(itemId)
                .userId(userId)
                .star(star)
                .content(content)
                .build();
    }
}
