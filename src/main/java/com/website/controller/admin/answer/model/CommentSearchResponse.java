package com.website.controller.admin.answer.model;

import com.website.service.admin.answer.model.CommentSearchResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSearchResponse {
    private Long commentId;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    private LocalDateTime commentUpdatedAt;
    private Long itemId;
    private Long userId;

    public static CommentSearchResponse of(CommentSearchResponseDto dto) {
        return CommentSearchResponse.builder()
                .commentId(dto.getCommentId())
                .commentContent(dto.getCommentContent())
                .commentCreatedAt(dto.getCommentCreatedAt())
                .commentUpdatedAt(dto.getCommentUpdatedAt())
                .itemId(dto.getItemId())
                .userId(dto.getUserId())
                .build();
    }
}
