package com.website.controller.api.review.model;

import com.website.service.review.model.ReviewUpdateDto;

public class ReviewUpdateRequest {
    private Integer star;
    private String content;

    public ReviewUpdateDto toDto(Long userId, Long itemId) {
        return ReviewUpdateDto.builder()
                .userId(userId)
                .star(star)
                .content(content)
                .itemId(itemId)
                .build();
    }
}
