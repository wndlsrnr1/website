package com.website.customer.controller.comment.model;

import com.website.customer.service.comment.CommentCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCreateRequest {
    private Long itemId;
    private Integer star;
    private String content;

    public CommentCreateDto toDto(Long userId) {
        return CommentCreateDto.builder()
                .itemId(itemId)
                .userId(userId)
                .star(star)
                .content(content)
                .build();
    }
}
