package com.website.customer.controller.comment.model;

import com.website.customer.service.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private Long itemId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponse of(CommentDto commentDto) {
        return CommentResponse.builder()
                .id(commentDto.getId())
                .itemId(commentDto.getItemId())
                .username(commentDto.getUsername())
                .content(commentDto.getContent())
                .createdAt(commentDto.getCreatedAt())
                .updatedAt(commentDto.getUpdatedAt())
                .build();
    }
}
