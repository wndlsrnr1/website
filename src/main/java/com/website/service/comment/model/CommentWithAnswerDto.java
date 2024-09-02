package com.website.service.comment.model;

import com.website.controller.api.comment.model.CommentWithAnswerResponse;
import com.website.repository.comment.model.CommentWithAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentWithAnswerDto {
    private Long id;
    private Long itemId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long answerId;

    public static CommentWithAnswerDto of(CommentWithAnswer comment) {
        return CommentWithAnswerDto.builder()
                .id(comment.getId())
                .itemId(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .answerId(comment.getAnswerId())
                .build();
    }
}
