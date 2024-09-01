package com.website.service.admin.answer.model;

import com.website.repository.answer.model.CommentSearchWithAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSearchWithAnswerResponseDto {
    private Long commentId;
    private String commentContent;
    private LocalDateTime commentCreatedAt;
    private LocalDateTime commentUpdatedAt;
    private Long itemId;
    private Long userId;
    private Long answerId;

    public static CommentSearchWithAnswerResponseDto of(CommentSearchWithAnswer fetchResult) {
        return CommentSearchWithAnswerResponseDto.builder()
                .commentId(fetchResult.getCommentId())
                .commentContent(fetchResult.getCommentContent())
                .commentCreatedAt(fetchResult.getCommentCreatedAt())
                .commentUpdatedAt(fetchResult.getCommentUpdatedAt())
                .itemId(fetchResult.getItemId())
                .userId(fetchResult.getUserId())
                .answerId(fetchResult.getAnswerId())
                .build();
    }
}
