package com.website.admin.controller.answer.model;

import com.website.admin.service.answer.model.CommentSearchWithAnswerResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSearchWithAnswerResponse {
    private Long commentId;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    private LocalDateTime commentUpdatedAt;
    private Long itemId;
    private Long userId;
    private Long answerId;

    public static CommentSearchWithAnswerResponse of(CommentSearchWithAnswerResponseDto dto) {
        return CommentSearchWithAnswerResponse.builder()
                .commentId(dto.getCommentId())
                .commentContent(dto.getCommentContent())
                .commentCreatedAt(dto.getCommentCreatedAt())
                .commentUpdatedAt(dto.getCommentUpdatedAt())
                .itemId(dto.getItemId())
                .userId(dto.getUserId())
                .answerId(dto.getAnswerId())
                .build();
    }
}
